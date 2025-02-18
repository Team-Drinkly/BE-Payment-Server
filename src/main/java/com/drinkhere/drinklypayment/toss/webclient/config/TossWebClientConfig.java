package com.drinkhere.drinklypayment.toss.webclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class TossWebClientConfig {

    @Bean
    public WebClient authorizeBillingWebClient(WebClient.Builder builder) {
        return builder.baseUrl("https://api.tosspayments.com/v1/billing/authorizations/issue").build();
    }

    @Bean
    public WebClient autoPaymentWebClient(WebClient.Builder builder) {
        return builder.baseUrl("https://api.tosspayments.com/v1/billing").build();
    }

    @Bean
    public WebClient cancelPaymentWebClient(WebClient.Builder builder) {
        return builder.baseUrl("https://api.tosspayments.com/v1/payments").build();
    }
}
