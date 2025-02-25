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

        boolean exists = couponRepository.existsByMemberIdAndTypeAndStatus(memberId, type, CouponStatus.AVAILABLE);
        if (exists) {
            throw new CouponException(CouponErrorCode.COUPON_ALREADY_ISSUED);
        }

        SubscriptionCoupon coupon = SubscriptionCoupon.builder()
                .memberId(memberId)
                .type(type)
                .status(CouponStatus.AVAILABLE)
                .isUsed(false)
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

    /**
     * 사용 가능한 쿠폰 조회
     */
    @Transactional(readOnly = true)
    public List<SubscriptionCoupon> getAvailableCoupons(Long memberId) {
        return couponRepository.findByMemberIdAndIsUsed(memberId, false);
    }

    /**
     * 사용 완료된 쿠폰 조회
     */
    @Transactional(readOnly = true)
    public List<SubscriptionCoupon> getUsedCoupons(Long memberId) {
        return couponRepository.findByMemberIdAndIsUsed(memberId, true);
    }
}
