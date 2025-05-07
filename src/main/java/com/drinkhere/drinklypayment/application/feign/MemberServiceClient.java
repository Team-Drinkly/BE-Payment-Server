package com.drinkhere.drinklypayment.application.feign;

import com.drinkhere.drinklypayment.common.response.ApplicationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "member-service", url = "${member-service.url}")
public interface MemberServiceClient {

    @GetMapping("/check-subscription")
    ApplicationResponse<Boolean> isMemberSubscribed(@RequestHeader("member-id") Long memberId);

    @PostMapping("/subscribe-update")
    void updateSubscriptionStatus(
            @RequestHeader("member-id") Long memberId,
            @RequestParam("subscription-history-id") Long subscriptionHistoryId,
            @RequestParam("duration-days") int durationDays
    );

    @GetMapping("/subscribedlist")
    ApplicationResponse<Boolean> checkDeactivatedSubscribed(Long memberId);
}
