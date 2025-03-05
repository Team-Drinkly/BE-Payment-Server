package com.drinkhere.drinklypayment.domain.subscription.service;

import com.drinkhere.drinklypayment.application.feign.MemberServiceClient;
import com.drinkhere.drinklypayment.common.exception.coupon.CouponErrorCode;
import com.drinkhere.drinklypayment.common.exception.coupon.CouponException;
import com.drinkhere.drinklypayment.common.exception.subscription.SubscriptionErrorCode;
import com.drinkhere.drinklypayment.common.exception.subscription.SubscriptionException;
import com.drinkhere.drinklypayment.domain.subscription.dto.SubscriptionCouponDto;
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
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SubscriptionCouponService {

    private final SubscriptionCouponRepository couponRepository;
    private final SubscriptionHistoryRepository historyRepository;
    private final MemberServiceClient memberServiceClient;

    /**
     * 특별 구독 쿠폰 지급 (쿠폰 ID 반환)
     */
    public Long issueCoupon(Long memberId, CouponType type) {

        boolean exists = couponRepository.existsByMemberIdAndType(memberId, type);
        if (exists) {
            throw new CouponException(CouponErrorCode.COUPON_ALREADY_ISSUED);
        }

        LocalDateTime expirationDate = LocalDateTime.now().plusDays(30); // 30일 후 만료

        SubscriptionCoupon coupon = SubscriptionCoupon.builder()
                .memberId(memberId)
                .type(type)
                .status(CouponStatus.AVAILABLE)
                .isUsed(false)
                .expirationDate(expirationDate)
                .build();

        couponRepository.save(coupon);
        return coupon.getId();
    }

    /**
     * 특별 구독 쿠폰 사용 (쿠폰 ID 선택)
     */
    public void useCoupon(Long memberId, Long couponId) {

        boolean isSubscribed = memberServiceClient.isMemberSubscribed(memberId).getPayload();
        if (isSubscribed) {
            throw new SubscriptionException(SubscriptionErrorCode.SUBSCRIPTION_ALREADY_ACTIVE);
        }

        SubscriptionCoupon coupon = couponRepository.findByIdAndMemberIdAndStatus(couponId, memberId, CouponStatus.AVAILABLE)
                .orElseThrow(() -> new CouponException(CouponErrorCode.COUPON_NOT_FOUND));

        coupon.useCoupon();

        int durationDays = switch (coupon.getType()) {
            case RESERVE -> 30;
            case INITIAL -> 7;
        };

        LocalDateTime startDate = LocalDateTime.now();
        SubscriptionHistory history = SubscriptionHistory.builder()
                .memberId(memberId)
                .startDate(startDate)
                .durationDays(durationDays)
                .build();

        historyRepository.save(history);
        memberServiceClient.updateSubscriptionStatus(memberId, history.getId(), durationDays);
    }

    @Transactional(readOnly = true)
    public List<SubscriptionCouponDto> getAvailableCoupons(Long memberId) {
        return couponRepository.findByMemberIdAndIsUsed(memberId, false)
                .stream()
                .filter(coupon -> !coupon.isExpired()) // 만료된 쿠폰 제외
                .map(SubscriptionCouponDto::new) // DTO 변환
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SubscriptionCouponDto> getUsedCoupons(Long memberId) {
        return couponRepository.findByMemberIdAndIsUsed(memberId, true)
                .stream()
                .map(SubscriptionCouponDto::new) // DTO 변환
                .toList();
    }

    /**
     * 현재 날짜를 기준으로 만료된 쿠폰 ID 조회
     */
    @Transactional(readOnly = true)
    public List<Long> getExpiredCouponIds() {
        return couponRepository.findExpiredCoupons(LocalDateTime.now());
    }

    /**
     * 만료된 쿠폰 상태 변경
     */
    public void expireCoupons(List<Long> couponIds) {
        couponRepository.updateCouponsToExpired(couponIds);
    }
}
