package com.drinkhere.drinklypayment.common.exception;

import com.drinkhere.drinklypayment.common.response.ApplicationResponse;
import org.springframework.http.HttpStatus;

public interface BaseErrorCode {

    HttpStatus getHttpStatus();

    String getMessage();

    ApplicationResponse<String> toResponseEntity();
}