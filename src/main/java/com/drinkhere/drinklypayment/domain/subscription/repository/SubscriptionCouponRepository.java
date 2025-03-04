package com.drinkhere.drinklypayment.domain.subscription.repository;

import com.drinkhere.drinklypayment.domain.subscription.entity.CouponStatus;
import com.drinkhere.drinklypayment.domain.subscription.entity.CouponType;
import com.drinkhere.drinklypayment.domain.subscription.entity.SubscriptionCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SubscriptionCouponRepository extends JpaRepository<SubscriptionCoupon, Long> {

    Optional<SubscriptionCoupon> findByIdAndMemberIdAndStatus(Long couponId, Long memberId, CouponStatus status);

    List<SubscriptionCoupon> findByMemberIdAndIsUsed(Long memberId, boolean isUsed);

    boolean existsByMemberIdAndTypeAndStatus(Long memberId, CouponType type, CouponStatus status);

    /**
     * 현재 날짜를 기준으로 만료된 쿠폰 ID 조회
     */
    @Query("SELECT c.id FROM SubscriptionCoupon c WHERE c.expirationDate < :now AND c.status = 'AVAILABLE'")
    List<Long> findExpiredCoupons(LocalDateTime now);

    /**
     * 쿠폰 상태를 만료(사용 완료)로 변경
     */
    @Modifying
    @Query("UPDATE SubscriptionCoupon c SET c.status = 'EXPIRED', c.isUsed = true WHERE c.id IN :couponIds")
    void updateCouponsToExpired(List<Long> couponIds);
}
