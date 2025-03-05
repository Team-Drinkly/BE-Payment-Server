package com.drinkhere.drinklypayment.application.subscription.presentation;

import com.drinkhere.drinklypayment.common.response.ApplicationResponse;
import com.drinkhere.drinklypayment.domain.subscription.dto.SubscriptionCouponDto;
import com.drinkhere.drinklypayment.domain.subscription.entity.CouponType;
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
    public ApplicationResponse<List<SubscriptionCouponDto>> getAvailableCoupons(
            @RequestHeader("member-id") String memberId
    ) {
        List<SubscriptionCouponDto> response = couponService.getAvailableCoupons(Long.valueOf(memberId));
        return ApplicationResponse.ok(response);
    }

    @GetMapping("/coupons/used")
    public ApplicationResponse<List<SubscriptionCouponDto>> getUsedCoupons(
            @RequestHeader("member-id") String memberId
    ) {
        List<SubscriptionCouponDto> response = couponService.getUsedCoupons(Long.valueOf(memberId));
        return ApplicationResponse.ok(response);
    }

    /**
     * 만료된 쿠폰 ID 조회
     */
    @GetMapping("/coupons/expired")
    public ApplicationResponse<List<Long>> getExpiredCoupons() {
        return ApplicationResponse.ok(couponService.getExpiredCouponIds());
    }

    /**
     * 만료된 쿠폰 처리
     */
    @PostMapping("/coupons/expire")
    public ApplicationResponse<String> expireCoupons(@RequestBody List<Long> couponIds) {
        couponService.expireCoupons(couponIds);
        return ApplicationResponse.ok("만료된 쿠폰 처리 완료");
    }
}
