package com.example.paymentprovider.service;

import com.example.paymentprovider.entity.Status;
import com.example.paymentprovider.entity.Transaction;
import com.example.paymentprovider.entity.TransactionType;
import com.example.paymentprovider.repository.CardRepository;
import com.example.paymentprovider.repository.CustomerRepository;
import com.example.paymentprovider.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CardRepository cardRepository;
    private final CustomerRepository customerRepository;

    public Mono<Transaction> createTopUpTransaction(Transaction transaction) {
        return customerRepository.findByExternalId(transaction.getCustomer().getExternalId())
                .switchIfEmpty(customerRepository.save(transaction.getCustomer()))
                .then(cardRepository.findByCardNumber(transaction.getCard().getCardNumber())
                        .switchIfEmpty(cardRepository.save(transaction.getCard())))
                .then(transactionRepository.save(transaction.toBuilder()
                        .status(Status.IN_PROGRESS)
                        .messageTr("OK")
                        .customerId(transaction.getCustomer().getExternalId())
                        .createdAt(LocalDateTime.now())
                        .build()));
    }

    public Flux<Transaction> getTransactionList(LocalDateTime startDate, LocalDateTime endDate, TransactionType transactionType) {
        if (startDate == null && endDate == null) {
            return transactionRepository.findByCreatedAt(LocalDate.now(), transactionType);
        } else {
            return transactionRepository.findBetweenDates(startDate, endDate, transactionType);
        }
    }

    public Mono<Transaction> getTransactionById(String id) {
        return transactionRepository.findById(id);
    }
}
