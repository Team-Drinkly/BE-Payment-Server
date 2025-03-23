package com.drinkhere.drinklypayment.domain.subscription.repository;

import com.drinkhere.drinklypayment.domain.subscription.entity.SubscriptionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionHistoryRepository extends JpaRepository<SubscriptionHistory, Long> {

    Optional<SubscriptionHistory> findByMemberIdAndOrderId(Long memberId, String orderId);
}
