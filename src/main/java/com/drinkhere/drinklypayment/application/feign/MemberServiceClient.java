package com.drinkhere.drinklypayment.application.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "member-service", url = "http://member-service/api/v1/member/m")
public interface MemberServiceClient {

    @GetMapping("/subscribe-check")
    boolean isMemberSubscribed(@RequestHeader("member-id") Long memberId);

    @PostMapping("/subscribe-update")
    void updateSubscriptionStatus(
            @RequestHeader("member-id") Long memberId,
            @RequestParam("subscription-history-id") Long subscriptionHistoryId,
            @RequestParam("duration-days") int durationDays
    );
}
