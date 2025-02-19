package com.drinkhere.drinklypayment.domain.repository;

import com.drinkhere.drinklypayment.domain.entity.SubscriptionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionHistoryRepository extends JpaRepository<SubscriptionHistory, Long> {
}
