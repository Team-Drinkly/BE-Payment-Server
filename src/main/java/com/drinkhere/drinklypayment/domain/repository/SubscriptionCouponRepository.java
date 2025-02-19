package com.drinkhere.drinklypayment.domain.repository;

import com.drinkhere.drinklypayment.domain.entity.CouponStatus;
import com.drinkhere.drinklypayment.domain.entity.SubscriptionCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionCouponRepository extends JpaRepository<SubscriptionCoupon, Long> {

    Optional<SubscriptionCoupon> findByMemberIdAndStatus(Long memberId, CouponStatus status);
}
