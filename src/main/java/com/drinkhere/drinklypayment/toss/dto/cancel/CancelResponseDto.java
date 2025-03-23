package com.drinkhere.drinklypayment.toss.dto.cancel;

import lombok.Builder;

@Builder
public record CancelResponseDto(
        String paymentKey,
        String status,
        String message
) {
}
