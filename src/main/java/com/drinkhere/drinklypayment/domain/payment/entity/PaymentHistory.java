package com.drinkhere.drinklypayment.domain.payment.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_history_id")
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private String billingKey;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private String status; // SUCCESS, FAILED

    @Column(nullable = false)
    private String orderId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public PaymentHistory(Long memberId, String billingKey, int amount, String status, String orderId, LocalDateTime createdAt) {
        this.memberId = memberId;
        this.billingKey = billingKey;
        this.amount = amount;
        this.status = status;
        this.orderId = orderId;
        this.createdAt = createdAt;
    }
}
