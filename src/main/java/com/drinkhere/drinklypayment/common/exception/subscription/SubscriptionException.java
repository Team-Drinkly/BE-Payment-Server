package com.drinkhere.drinklypayment.common.exception.subscription;

import lombok.Getter;

/**
 * 구독 관련 커스텀 예외
 */
@Getter
public class SubscriptionException extends RuntimeException {

    private final SubscriptionErrorCode errorCode;

    public SubscriptionException(SubscriptionErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
