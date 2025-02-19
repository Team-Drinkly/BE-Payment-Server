package com.drinkhere.drinklypayment.domain.subscription.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubscriptionCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private com.drinkhere.drinklypayment.domain.subscription.entity.CouponType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private com.drinkhere.drinklypayment.domain.subscription.entity.CouponStatus status;

    public void useCoupon() {
        this.status = com.drinkhere.drinklypayment.domain.subscription.entity.CouponStatus.USED;
    }

    @Builder
    public SubscriptionCoupon(Long memberId, com.drinkhere.drinklypayment.domain.subscription.entity.CouponType type, com.drinkhere.drinklypayment.domain.subscription.entity.CouponStatus status) {
        this.memberId = memberId;
        this.type = type;
        this.status = status;
    }
}
