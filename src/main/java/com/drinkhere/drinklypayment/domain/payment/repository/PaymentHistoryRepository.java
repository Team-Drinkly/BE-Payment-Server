package com.drinkhere.drinklypayment.domain.payment.repository;

import com.drinkhere.drinklypayment.domain.payment.entity.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long> {
}
