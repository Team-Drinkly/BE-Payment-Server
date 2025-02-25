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
    private CouponType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponStatus status;

    @Column(nullable = false)
    private boolean isUsed;  // 쿠폰 사용 여부 추가

    public void useCoupon() {
        this.status = CouponStatus.USED;
        this.isUsed = true;  // 사용 여부 업데이트
    }

    @Builder
    public SubscriptionCoupon(Long memberId, CouponType type, CouponStatus status, boolean isUsed) {
        this.memberId = memberId;
        this.type = type;
        this.status = status;
        this.isUsed = isUsed;
    }
}
