package com.vr.miniautorizador.repository;
import com.vr.miniautorizador.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {
    Optional<Card> findByNumber(String cardNumber);
}