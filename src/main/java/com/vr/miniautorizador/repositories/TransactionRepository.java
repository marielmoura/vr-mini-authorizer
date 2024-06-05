package com.vr.miniautorizador.repositories;

import com.vr.miniautorizador.models.Transaction;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.card.number = :cardNumber")
    Double sumAmountsByCardNumber(@Param("cardNumber") String cardNumber);
}