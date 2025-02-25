package com.drinkhere.drinklypayment.domain.subscription.repository;

import com.drinkhere.drinklypayment.domain.subscription.entity.CouponStatus;
import com.drinkhere.drinklypayment.domain.subscription.entity.CouponType;
import com.drinkhere.drinklypayment.domain.subscription.entity.SubscriptionCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionCouponRepository extends JpaRepository<SubscriptionCoupon, Long> {

    Optional<SubscriptionCoupon> findByIdAndMemberIdAndStatus(Long couponId, Long memberId, CouponStatus status);

    List<SubscriptionCoupon> findByMemberIdAndIsUsed(Long memberId, boolean isUsed);

    boolean existsByMemberIdAndTypeAndStatus(Long memberId, CouponType type, CouponStatus status);
}
