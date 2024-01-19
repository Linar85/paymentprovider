package com.example.paymentprovider.repository;

import com.example.paymentprovider.entity.Status;
import com.example.paymentprovider.entity.Transaction;
import com.example.paymentprovider.entity.TransactionType;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface TransactionRepository extends R2dbcRepository<Transaction, String> {

    @Query("select * from transactions tr where DATE(created_at) = :date and tr.transaction_type = :transactionType")
    Flux<Transaction> findByCreatedAt(LocalDate date, TransactionType transactionType);

    Flux<Transaction> findByStatusAndTransactionType(Status status, TransactionType transactionType);

    @Query(value = "SELECT * FROM transactions tr WHERE tr.created_at > :startDate and tr.created_at < :endDate and tr.transaction_type = :transactionType")
    Flux<Transaction> findBetweenDates(@Param("startDate") LocalDateTime startDate,
                                       @Param("endDate") LocalDateTime endDate,
                                       @Param("transactionType") TransactionType transactionType);

}
