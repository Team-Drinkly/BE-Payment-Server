package com.drinkhere.drinklypayment.toss.dto.cancel;

public record CancelResponseDto(String paymentKey, String cancelReason, String requestedAt, String approvedAt,
                                int cancelAmount) {
}
