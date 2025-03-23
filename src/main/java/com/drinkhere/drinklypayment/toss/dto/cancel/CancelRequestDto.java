package com.drinkhere.drinklypayment.toss.dto.cancel;

public record CancelRequestDto(
        String paymentKey,
        String orderId,
        String cancelReason
) {
}
