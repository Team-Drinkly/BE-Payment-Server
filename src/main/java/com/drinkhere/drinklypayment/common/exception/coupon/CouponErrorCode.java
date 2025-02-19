package com.drinkhere.drinklypayment.common.exception.coupon;

import com.drinkhere.drinklypayment.common.exception.BaseErrorCode;
import com.drinkhere.drinklypayment.common.response.ApplicationResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 쿠폰 관련 예외 처리
 */
@Getter
@RequiredArgsConstructor
public enum CouponErrorCode implements BaseErrorCode {

    COUPON_NOT_FOUND(HttpStatus.NOT_FOUND, "사용 가능한 쿠폰이 없습니다."),
    COUPON_ALREADY_USED(HttpStatus.BAD_REQUEST, "이미 사용된 쿠폰입니다."),
    COUPON_EXPIRED(HttpStatus.BAD_REQUEST, "쿠폰이 만료되었습니다."),
    COUPON_ISSUANCE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "쿠폰 발급 중 오류가 발생했습니다."),
    COUPON_USE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "쿠폰 사용 중 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public ApplicationResponse<String> toResponseEntity() {
        return ApplicationResponse.server(message);
    }
}
