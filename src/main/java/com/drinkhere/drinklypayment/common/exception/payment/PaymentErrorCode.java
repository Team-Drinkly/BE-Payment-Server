package com.drinkhere.drinklypayment.common.exception.payment;

import com.drinkhere.drinklypayment.common.exception.BaseErrorCode;
import com.drinkhere.drinklypayment.common.response.ApplicationResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PaymentErrorCode implements BaseErrorCode {

    PAYMENT_FAILED(HttpStatus.BAD_REQUEST, "결제에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public ApplicationResponse<String> toResponseEntity() {
        return ApplicationResponse.server(message);
    }
}
