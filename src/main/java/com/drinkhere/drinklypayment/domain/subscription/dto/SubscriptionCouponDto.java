package com.drinkhere.drinklypayment.domain.subscription.dto;

import com.drinkhere.drinklypayment.domain.subscription.entity.CouponStatus;
import com.drinkhere.drinklypayment.domain.subscription.entity.CouponType;
import com.drinkhere.drinklypayment.domain.subscription.entity.SubscriptionCoupon;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SubscriptionCouponDto {
    private final Long id;
    private final Long memberId;
    private final CouponType type;
    private final CouponStatus status;
    private final boolean used;
    private final LocalDateTime expirationDate;
    private final String title;
    private final String description;

    public SubscriptionCouponDto(SubscriptionCoupon coupon) {
        this.id = coupon.getId();
        this.memberId = coupon.getMemberId();
        this.type = coupon.getType();
        this.status = coupon.getStatus();
        this.used = coupon.isUsed();
        this.expirationDate = coupon.getExpirationDate();
        this.title = coupon.getTitle();
        this.description = coupon.getDescription();
    }
}
