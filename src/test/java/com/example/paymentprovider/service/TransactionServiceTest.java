package com.example.paymentprovider.service;

import com.example.paymentprovider.entity.Card;
import com.example.paymentprovider.entity.Customer;
import com.example.paymentprovider.entity.Transaction;
import com.example.paymentprovider.entity.TransactionType;
import com.example.paymentprovider.repository.CardRepository;
import com.example.paymentprovider.repository.CustomerRepository;
import com.example.paymentprovider.repository.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(SpringExtension.class)
class TransactionServiceTest {

    @InjectMocks
    TransactionService transactionService;
    @Mock
    CardRepository cardRepository;
    @Mock
    CustomerRepository customerRepository;
    @Mock
    TransactionRepository transactionRepository;

    Customer customer = Customer.builder()
            .externalId(1L)
            .build();

    Card card = Card.builder()
            .cardNumber(123456789L)
            .build();

    Transaction transaction = Transaction.builder()
            .id("test_id")
            .externalTransactionId("external_id")
            .customer(customer)
            .card(card)
            .amount(1000)
            .build();

    @Test
    void createTopUpTransaction() {

        Mockito.when(customerRepository.findByExternalId(any())).thenReturn(Mono.empty());
        Mockito.when(customerRepository.save(customer)).thenReturn(Mono.just(customer));
        Mockito.when(cardRepository.findByCardNumber(any())).thenReturn(Mono.empty());
        Mockito.when(cardRepository.save(card)).thenReturn(Mono.just(card));
        Mockito.when(transactionRepository.save(any(Transaction.class))).thenReturn(Mono.just(transaction));

        StepVerifier.create(transactionService.createTopUpTransaction(transaction))
                .consumeNextWith(tr->
                        Assertions.assertEquals(tr.getId(), transaction.getId()))
                .verifyComplete();

        Mockito.verify(customerRepository, Mockito.times(1)).save(any(Customer.class));
        Mockito.verify(cardRepository, Mockito.times(1)).save(any(Card.class));

    }

    @Test
    void getTransactionListToday() {
        Mockito.when(transactionRepository.findByCreatedAt(eq(LocalDate.now()), any(TransactionType.class))).thenReturn(Flux.just(transaction));

        StepVerifier.create(transactionService.getTransactionList(null, null, TransactionType.TOP_UP))
                .expectNextCount(1L)
                .verifyComplete();
    }

    @Test
    void getBetweenDaysTransactionListToday() {

        LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 1, 2, 0, 0);

        Mockito.when(transactionRepository.findBetweenDates(any(LocalDateTime.class), any(LocalDateTime.class), any(TransactionType.class)))
                .thenReturn(Flux.just(transaction, transaction));

        StepVerifier.create(transactionService.getTransactionList(startDate, endDate, TransactionType.TOP_UP))
                .expectNextCount(2L)
                .verifyComplete();
    }

    @Test
    void getTransactionById() {

        String actualId = "test_id";

        Mockito.when(transactionRepository.findById(actualId)).thenReturn(Mono.just(transaction));

        StepVerifier.create(transactionService.getTransactionById(actualId))
                .consumeNextWith(tr -> {
                    Assertions.assertEquals(tr.getId(), actualId);
                })
                .verifyComplete();
    }
}