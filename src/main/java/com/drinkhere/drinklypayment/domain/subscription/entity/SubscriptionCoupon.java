package com.drinkhere.drinklypayment.domain.subscription.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubscriptionCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponStatus status;

    @Column(nullable = false)
    private boolean isUsed;

    @Column(nullable = false)
    private LocalDateTime expirationDate; // 유효기간 추가

    public void useCoupon() {
        this.status = CouponStatus.USED;
        this.isUsed = true;
    }

    /**
     * 쿠폰이 만료되었는지 확인 (현재 시간이 expirationDate 이후인지 체크)
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expirationDate);
    }

    public String getFormattedExpirationDate() {
        return expirationDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    @Builder
    public SubscriptionCoupon(Long memberId, CouponType type, CouponStatus status, boolean isUsed, LocalDateTime expirationDate) {
        this.memberId = memberId;
        this.type = type;
        this.status = status;
        this.isUsed = isUsed;
        this.expirationDate = expirationDate;
    }
}
