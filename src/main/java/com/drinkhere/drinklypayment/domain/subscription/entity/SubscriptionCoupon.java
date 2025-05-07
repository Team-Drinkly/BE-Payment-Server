package com.drinkhere.drinklypayment.domain.subscription.entity;

import com.drinkhere.drinklypayment.domain.auditing.BaseTimeEntity;
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
public class SubscriptionCoupon extends BaseTimeEntity {

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

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 500)
    private String description;

    public void useCoupon() {
        this.status = CouponStatus.USED;
        this.isUsed = true;
    }

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

        if (type == CouponType.RESERVE) {
            this.title = "사전예약 이벤트 30일 구독권";
            this.description = "드링클리 사전 예약 이벤트에 참여한 분에게 드리는 구독권이에요";
        } else if (type == CouponType.INITIAL) {
            this.title = "앱 출시 이벤트 30일 구독권";
            this.description = "드링클리 출시 기념 이벤트로 회원가입한 유저분에게만 드리는 구독권이에요";
        }
    }
}
