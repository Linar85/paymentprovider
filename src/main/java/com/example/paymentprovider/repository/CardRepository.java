package com.example.paymentprovider.repository;

import com.example.paymentprovider.entity.Card;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CardRepository extends R2dbcRepository<Card, Long> {
    Mono<Card> findByCardNumber(Long cardNumber);
}
