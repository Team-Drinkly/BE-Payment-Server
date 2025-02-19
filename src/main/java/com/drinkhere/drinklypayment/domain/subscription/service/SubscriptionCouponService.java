package com.drinkhere.drinklypayment.domain.subscription.service;

import com.drinkhere.drinklypayment.application.feign.MemberServiceClient;
import com.drinkhere.drinklypayment.common.exception.coupon.CouponErrorCode;
import com.drinkhere.drinklypayment.common.exception.coupon.CouponException;
import com.drinkhere.drinklypayment.common.exception.subscription.SubscriptionErrorCode;
import com.drinkhere.drinklypayment.common.exception.subscription.SubscriptionException;
import com.drinkhere.drinklypayment.domain.subscription.entity.CouponStatus;
import com.drinkhere.drinklypayment.domain.subscription.entity.CouponType;
import com.drinkhere.drinklypayment.domain.subscription.entity.SubscriptionCoupon;
import com.drinkhere.drinklypayment.domain.subscription.entity.SubscriptionHistory;
import com.drinkhere.drinklypayment.domain.subscription.repository.SubscriptionCouponRepository;
import com.drinkhere.drinklypayment.domain.subscription.repository.SubscriptionHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SubscriptionCouponService {

    private final SubscriptionCouponRepository couponRepository;
    private final SubscriptionHistoryRepository historyRepository;
    private final MemberServiceClient memberServiceClient;

    /**
     * 특별 구독 쿠폰 지급
     */
    @Transactional
    public void issueCoupon(Long memberId, CouponType type) {
        SubscriptionCoupon coupon = SubscriptionCoupon.builder()
                .memberId(memberId)
                .type(type)
                .status(CouponStatus.AVAILABLE)
                .build();

        couponRepository.save(coupon);
    }

    /**
     * 특별 구독 쿠폰 사용 (구독 여부 체크 추가)
     */
    @Transactional
    public void useCoupon(Long memberId) {

        // 현재 사용자가 구독 중인지 확인
        boolean isSubscribed = memberServiceClient.isMemberSubscribed(memberId);
        if (isSubscribed) {
            throw new SubscriptionException(SubscriptionErrorCode.SUBSCRIPTION_ALREADY_ACTIVE);
        }

        // 사용 가능한 쿠폰 조회
        SubscriptionCoupon coupon = couponRepository.findByMemberIdAndStatus(memberId, CouponStatus.AVAILABLE)
                .orElseThrow(() -> new CouponException(CouponErrorCode.COUPON_NOT_FOUND));

        coupon.useCoupon();

        // 쿠폰 유형에 따라 구독 기간 설정
        int durationDays = switch (coupon.getType()) {
            case RESERVE -> 30;  // RESERVE(사전예약)일 경우 30일
            case INITIAL -> 7;    // INITIAL(앱 설치)일 경우 7일
        };

        // 1. 구독 이력 저장 (시작일, 구독 기간만 저장)
        LocalDateTime startDate = LocalDateTime.now();

        SubscriptionHistory history = SubscriptionHistory.builder()
                .memberId(memberId)
                .startDate(startDate)
                .durationDays(durationDays)
                .build();

        historyRepository.save(history);

        // 2. 구독 이력 ID를 멤버 서비스로 전달하여 구독 등록 요청
        memberServiceClient.updateSubscriptionStatus(memberId, history.getId(), durationDays);
    }
}
