package com.vr.miniautorizador.repository;
import com.vr.miniautorizador.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}