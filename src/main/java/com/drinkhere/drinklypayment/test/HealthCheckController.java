package com.drinkhere.drinklypayment.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HealthCheckController {

    @GetMapping("/health-check")
    public String status() {
        return "Payment 마이크서비스 테스트입니다.";
    }
}
