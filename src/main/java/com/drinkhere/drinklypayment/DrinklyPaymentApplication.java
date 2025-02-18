package com.drinkhere.drinklypayment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaAuditing
@ConfigurationPropertiesScan
public class DrinklyPaymentApplication {

    public static void main(String[] args) {
        SpringApplication.run(DrinklyPaymentApplication.class, args);
    }

}
