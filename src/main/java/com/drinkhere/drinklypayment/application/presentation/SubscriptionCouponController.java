package com.drinkhere.drinklypayment.application.presentation;

import com.drinkhere.drinklypayment.domain.entity.CouponType;
import com.drinkhere.drinklypayment.domain.service.SubscriptionCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment/m")
@RequiredArgsConstructor
public class SubscriptionCouponController {

    private final SubscriptionCouponService couponService;

    /**
     * 특별 구독 쿠폰 지급 API
     */
    @PostMapping("/coupon-issue")
    public ResponseEntity<String> issueCoupon(
            @RequestHeader("member-id") String memberId,
            @RequestParam CouponType type
    ) {
        couponService.issueCoupon(Long.valueOf(memberId), type);
        return ResponseEntity.ok("쿠폰 지급 완료");
    }

    /**
     * 특별 구독 쿠폰 사용 API
     */
    @PostMapping("/coupon-use")
    public ResponseEntity<String> useCoupon(
            @RequestHeader("member-id") String memberId) {

        couponService.useCoupon(Long.valueOf(memberId));
        return ResponseEntity.ok("쿠폰 사용 완료");
    }
}

