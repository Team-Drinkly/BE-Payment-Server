package com.drinkhere.drinklypayment.domain.subscription.repository;

import com.drinkhere.drinklypayment.domain.subscription.entity.CouponStatus;
import com.drinkhere.drinklypayment.domain.subscription.entity.CouponType;
import com.drinkhere.drinklypayment.domain.subscription.entity.SubscriptionCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionCouponRepository extends JpaRepository<SubscriptionCoupon, Long> {

    Optional<SubscriptionCoupon> findByMemberIdAndStatus(Long memberId, CouponStatus status);

    boolean existsByMemberIdAndTypeAndStatus(Long memberId, CouponType type, CouponStatus status);
}
