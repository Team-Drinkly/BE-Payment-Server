package com.drinkhere.drinklypayment.domain.subscription.dto;

import com.drinkhere.drinklypayment.domain.subscription.entity.CouponStatus;
import com.drinkhere.drinklypayment.domain.subscription.entity.CouponType;
import com.drinkhere.drinklypayment.domain.subscription.entity.SubscriptionCoupon;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CouponResponse {
    private Long id;
    private Long memberId;
    private CouponType type;
    private CouponStatus status;
    private boolean used;
    private String expirationDate; // yyyy-MM-dd 형식

    public static CouponResponse fromEntity(SubscriptionCoupon coupon) {
        return CouponResponse.builder()
                .id(coupon.getId())
                .memberId(coupon.getMemberId())
                .type(coupon.getType())
                .status(coupon.getStatus())
                .used(coupon.isUsed())
                .expirationDate(coupon.getFormattedExpirationDate()) // 날짜 변환
                .build();
    }
}
