package com.drinkhere.drinklypayment.application.payment.presentation;

import com.drinkhere.drinklypayment.domain.payment.service.PaymentService;
import com.drinkhere.drinklypayment.toss.dto.cancel.CancelRequestDto;
import com.drinkhere.drinklypayment.toss.dto.cancel.CancelResponseDto;
import com.drinkhere.drinklypayment.toss.dto.payment.PaymentRequestDto;
import com.drinkhere.drinklypayment.toss.dto.payment.PaymentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment/m")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/process")
    public ResponseEntity<PaymentResponseDto> processPayment(
            @RequestHeader("member-id") String memberId,
            @RequestBody PaymentRequestDto requestDto) {

        PaymentResponseDto response = paymentService.processPayment(Long.valueOf(memberId), requestDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/cancel")
    public ResponseEntity<CancelResponseDto> cancelPayment(
            @RequestHeader("member-id") String memberId,
            @RequestBody CancelRequestDto requestDto) {

        CancelResponseDto response = paymentService.cancelPayment(Long.valueOf(memberId), requestDto);
        return ResponseEntity.ok(response);
    }
}
