package com.drinkhere.drinklypayment.domain.payment.service;

import com.drinkhere.drinklypayment.application.feign.MemberServiceClient;
import com.drinkhere.drinklypayment.common.exception.payment.PaymentErrorCode;
import com.drinkhere.drinklypayment.common.exception.payment.PaymentException;
import com.drinkhere.drinklypayment.common.exception.subscription.SubscriptionErrorCode;
import com.drinkhere.drinklypayment.common.exception.subscription.SubscriptionException;
import com.drinkhere.drinklypayment.domain.payment.entity.PaymentHistory;
import com.drinkhere.drinklypayment.domain.payment.entity.PaymentStatus;
import com.drinkhere.drinklypayment.domain.payment.repository.PaymentHistoryRepository;
import com.drinkhere.drinklypayment.domain.subscription.entity.SubscriptionHistory;
import com.drinkhere.drinklypayment.domain.subscription.repository.SubscriptionHistoryRepository;
import com.drinkhere.drinklypayment.toss.dto.cancel.CancelRequestDto;
import com.drinkhere.drinklypayment.toss.dto.cancel.CancelResponseDto;
import com.drinkhere.drinklypayment.toss.dto.payment.PaymentRequestDto;
import com.drinkhere.drinklypayment.toss.dto.payment.PaymentResponseDto;
import com.drinkhere.drinklypayment.toss.service.TossPaymentsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final TossPaymentsUseCase tossPaymentsUseCase;
    private final PaymentHistoryRepository paymentHistoryRepository;
    private final SubscriptionHistoryRepository subscriptionHistoryRepository;
    private final MemberServiceClient memberServiceClient;

    @Transactional
    public PaymentResponseDto processPayment(Long memberId, PaymentRequestDto requestDto) {

        // 현재 사용자가 구독 중인지 확인
        boolean isSubscribed = memberServiceClient.isMemberSubscribed(memberId).getPayload();
        if (isSubscribed) {
            throw new SubscriptionException(SubscriptionErrorCode.SUBSCRIPTION_ALREADY_ACTIVE);
        }

        // 결제 요청 (Toss Payments API 호출)
        PaymentResponseDto response;
        try {
            response = tossPaymentsUseCase.performAutoPayment(requestDto, "TossSecretKey");
        } catch (Exception e) {
            // 결제 실패 시, 결제 이력 테이블에 저장 후 예외 던짐
            savePaymentHistory(memberId, requestDto, PaymentStatus.FAILED);
            throw new PaymentException(PaymentErrorCode.PAYMENT_FAILED);
        }

        // 결제 성공 시, 결제 이력 저장
        savePaymentHistory(memberId, requestDto, PaymentStatus.SUCCESS);

        // 구독 이력 저장 (구독 기간: 30일)
        SubscriptionHistory history = SubscriptionHistory.builder()
                .memberId(memberId)
                .startDate(LocalDateTime.now())
                .durationDays(30) // 기본적으로 30일 구독 적용
                .build();

        subscriptionHistoryRepository.save(history);

        // 멤버 서비스에 구독 상태 업데이트 요청 (Feign)
        memberServiceClient.updateSubscriptionStatus(memberId, history.getId(), 30);

        return response;
    }

    @Transactional
    public CancelResponseDto cancelPayment(Long memberId, CancelRequestDto cancelRequestDto) {

        // 1. 결제 이력 확인
        PaymentHistory payment = paymentHistoryRepository.findByOrderId(cancelRequestDto.orderId())
                .orElseThrow(() -> new RuntimeException("결제 이력을 찾을 수 없습니다."));

        // 2. 결제일로부터 7일 지났는지 확인
        if (payment.getCreatedAt().isBefore(LocalDateTime.now().minusDays(7))) {
            throw new SubscriptionException(SubscriptionErrorCode.REFUND_TOO_LATE);
        }

        // 3. 구독 이력 확인
        SubscriptionHistory subscription = subscriptionHistoryRepository.findByMemberIdAndOrderId(memberId, cancelRequestDto.orderId())
                .orElseThrow(() -> new RuntimeException("구독 이력을 찾을 수 없습니다."));

        if (subscription.isUsed()) {
            throw new SubscriptionException(SubscriptionErrorCode.REFUND_ALREADY_USED);
        }

        // 4. Toss API에 결제 취소 요청
        CancelResponseDto cancelResponse = tossPaymentsUseCase.cancelPayment(cancelRequestDto, "TossSecretKey");

        // 5. 결제 이력 상태 업데이트
        payment.cancel();

        // 6. 구독 이력 삭제 또는 만료 처리
        subscriptionHistoryRepository.delete(subscription); // 또는 expired 처리

        // 7. 멤버 서비스에 구독 상태 해제 요청
        memberServiceClient.updateSubscriptionStatus(memberId, null, 0); // 구독 비활성화

        return cancelResponse;
    }


    private void savePaymentHistory(Long memberId, PaymentRequestDto requestDto, PaymentStatus status) {
        PaymentHistory history = PaymentHistory.builder()
                .memberId(memberId)
                .billingKey(requestDto.billingKey())
                .amount(requestDto.amount())
                .status(status)
                .orderId(requestDto.orderId())
                .createdAt(LocalDateTime.now())
                .build();

        paymentHistoryRepository.save(history);
    }
}
