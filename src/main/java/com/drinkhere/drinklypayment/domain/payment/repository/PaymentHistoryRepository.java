package com.drinkhere.drinklypayment.domain.payment.repository;

import com.drinkhere.drinklypayment.domain.payment.entity.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long> {

    Optional<PaymentHistory> findByOrderId(String orderId);
}
