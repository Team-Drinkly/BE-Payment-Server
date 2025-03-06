package com.drinkhere.drinklypayment.domain.subscription.dto;

import com.drinkhere.drinklypayment.domain.subscription.entity.CouponStatus;
import com.drinkhere.drinklypayment.domain.subscription.entity.CouponType;
import com.drinkhere.drinklypayment.domain.subscription.entity.SubscriptionCoupon;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
public class SubscriptionCouponDto {
    private final Long id;
    private final Long memberId;
    private final CouponType type;
    private final CouponStatus status;
    private final boolean used;
    private final String expirationDate;
    private final String title;
    private final String description;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public SubscriptionCouponDto(SubscriptionCoupon coupon) {
        this.id = coupon.getId();
        this.memberId = coupon.getMemberId();
        this.type = coupon.getType();
        this.status = coupon.getStatus();
        this.used = coupon.isUsed();
        this.expirationDate = coupon.getExpirationDate().format(FORMATTER);  // 변경: 포맷 적용
        this.title = coupon.getTitle();
        this.description = coupon.getDescription();
    }
}
