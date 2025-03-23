package com.drinkhere.drinklypayment.common.exception.subscription;

import com.drinkhere.drinklypayment.common.exception.BaseErrorCode;
import com.drinkhere.drinklypayment.common.response.ApplicationResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 구독 관련 예외 처리
 */
@Getter
@RequiredArgsConstructor
public enum SubscriptionErrorCode implements BaseErrorCode {

    SUBSCRIPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "구독 정보가 존재하지 않습니다."),
    SUBSCRIPTION_ALREADY_ACTIVE(HttpStatus.BAD_REQUEST, "이미 활성화된 구독이 있습니다."),
    INVALID_SUBSCRIPTION_DURATION(HttpStatus.BAD_REQUEST, "유효하지 않은 구독 기간입니다."),
    SUBSCRIPTION_UPDATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "구독 정보 업데이트 중 오류가 발생했습니다."),
    REFUND_TOO_LATE(HttpStatus.BAD_REQUEST, "환불 가능 기간(7일)을 초과했습니다."),
    REFUND_ALREADY_USED(HttpStatus.BAD_REQUEST, "이미 서비스를 사용하여 환불이 불가능합니다.");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public ApplicationResponse<String> toResponseEntity() {
        return ApplicationResponse.server(message);
    }
}
