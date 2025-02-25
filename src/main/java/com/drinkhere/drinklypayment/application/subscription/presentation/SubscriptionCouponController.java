package com.drinkhere.drinklypayment.application.subscription.presentation;

import com.drinkhere.drinklypayment.common.response.ApplicationResponse;
import com.drinkhere.drinklypayment.domain.subscription.entity.CouponType;
import com.drinkhere.drinklypayment.domain.subscription.entity.SubscriptionCoupon;
import com.drinkhere.drinklypayment.domain.subscription.service.SubscriptionCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payment/m")
@RequiredArgsConstructor
public class SubscriptionCouponController {

    private final SubscriptionCouponService couponService;

    @PostMapping("/coupon-issue")
    public ApplicationResponse<Long> issueCoupon(
            @RequestHeader("member-id") String memberId,
            @RequestParam CouponType type
    ) {
        Long couponId = couponService.issueCoupon(Long.valueOf(memberId), type);
        return ApplicationResponse.ok(couponId);
    }

    @PostMapping("/coupon-use")
    public ApplicationResponse<String> useCoupon(
            @RequestHeader("member-id") String memberId,
            @RequestParam Long couponId
    ) {
        couponService.useCoupon(Long.valueOf(memberId), couponId);
        return ApplicationResponse.ok("쿠폰 사용 완료");
    }

    @GetMapping("/coupons/available")
    public ApplicationResponse<List<SubscriptionCoupon>> getAvailableCoupons(
            @RequestHeader("member-id") String memberId
    ) {
        return ApplicationResponse.ok(couponService.getAvailableCoupons(Long.valueOf(memberId)));
    }

    @GetMapping("/coupons/used")
    public ApplicationResponse<List<SubscriptionCoupon>> getUsedCoupons(
            @RequestHeader("member-id") String memberId
    ) {
        return ApplicationResponse.ok(couponService.getUsedCoupons(Long.valueOf(memberId)));
    }
}
