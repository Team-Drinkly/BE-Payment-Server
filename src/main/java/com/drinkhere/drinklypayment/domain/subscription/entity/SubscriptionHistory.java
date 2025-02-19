package com.drinkhere.drinklypayment.domain.subscription.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubscriptionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_history_id", unique = true)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private LocalDateTime startDate;  // 구독 시작일

    @Column(nullable = false)
    private int durationDays;  // 구독 기간 (ex. 7일, 30일)

    @Builder
    public SubscriptionHistory(Long memberId, LocalDateTime startDate, int durationDays) {
        this.memberId = memberId;
        this.startDate = startDate;
        this.durationDays = durationDays;
    }
}

