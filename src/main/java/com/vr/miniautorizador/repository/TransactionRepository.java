package com.vr.miniautorizador.repository;
import com.vr.miniautorizador.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.card.number = :cardNumber")
    Double sumAmountsByCardNumber(@Param("cardNumber") String cardNumber);
}