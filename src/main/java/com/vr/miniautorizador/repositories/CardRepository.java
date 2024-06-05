package com.vr.miniautorizador.repositories;
import com.vr.miniautorizador.models.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {
    Optional<Card> findByNumber(String cardNumber);
}