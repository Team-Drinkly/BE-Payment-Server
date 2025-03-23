package com.drinkhere.drinklypayment.domain.subscription.entity;

import com.drinkhere.drinklypayment.domain.auditing.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubscriptionHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_history_id", unique = true)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private int durationDays;

    @Column(nullable = false)
    private boolean isUsed = false;

    @Builder
    public SubscriptionHistory(Long memberId, LocalDateTime startDate, int durationDays, boolean isUsed) {
        this.memberId = memberId;
        this.startDate = startDate;
        this.durationDays = durationDays;
        this.isUsed = isUsed;
    }

    public void markUsed() {
        this.isUsed = true;
    }
}
