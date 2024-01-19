package com.example.paymentprovider.controller;

import com.example.paymentprovider.config.SecurityConfig;
import com.example.paymentprovider.dto.TransactionDto;
import com.example.paymentprovider.dto.TransactionRsDto;
import com.example.paymentprovider.entity.*;
import com.example.paymentprovider.mapper.TransactionMapper;
import com.example.paymentprovider.mapper.TransactionRsMapper;
import com.example.paymentprovider.mapper.WebhookMapper;
import com.example.paymentprovider.repository.CardRepository;
import com.example.paymentprovider.repository.CustomerRepository;
import com.example.paymentprovider.repository.TransactionRepository;
import com.example.paymentprovider.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = PayoutController.class)
@Import({TransactionService.class, SecurityConfig.class})
@WithMockUser
class PayoutControllerTest {

    @Autowired
    WebTestClient webTestClient;
    @MockBean
    TransactionRsMapper transactionRsMapper;
    @MockBean
    TransactionMapper transactionMapper;
    @MockBean
    WebhookMapper webhookMapper;
    @MockBean
    TransactionRepository transactionRepository;
    @MockBean
    CardRepository cardRepository;
    @MockBean
    CustomerRepository customerRepository;

    Customer customer = Customer.builder()
            .externalId(1L)
            .build();

    Transaction transaction = Transaction.builder()
            .id("test_id")
            .externalTransactionId("external_id")
            .customer(customer)
            .card(new Card())
            .amount(1000)
            .build();

    TransactionDto transactionDto = TransactionDto.builder()
            .id("test_id")
            .externalTransactionId("external_id")
            .amount(1000)
            .build();

    TransactionRsDto transactionRsDto = TransactionRsDto.builder()
            .externalTransactionId("external_id")
            .messageTr("OK")
            .status(Status.IN_PROGRESS)
            .build();


    @Test
    void createPayoutTransaction() {
        Mockito.when(transactionMapper.map(any(TransactionDto.class))).thenReturn(transaction);
        Mockito.when(transactionRsMapper.map(transaction)).thenReturn(transactionRsDto);

        Mockito.when(customerRepository.findByExternalId(1L)).thenReturn(Mono.just(customer));
        Mockito.when(customerRepository.save(customer)).thenReturn(Mono.just(customer));
        Mockito.when(cardRepository.findByCardNumber(any())).thenReturn(Mono.just(new Card()));
        Mockito.when(cardRepository.save(any())).thenReturn(Mono.just(new Card()));
        Mockito.when(transactionRepository.save(any())).thenReturn(Mono.just(transaction));

        webTestClient.post().uri("/api/v1/payments/payout")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(transaction)
                .exchange()
                .expectStatus().isOk();

        Mockito.verify(transactionRepository, Mockito.times(1)).save(any(Transaction.class));
    }

    @Test
    void getPayoutsList() {

        Mockito.when(transactionRepository.findByCreatedAt(any(LocalDate.class), any(TransactionType.class))).thenReturn(Flux.just(transaction));
        Mockito.when(transactionMapper.map(transaction)).thenReturn(transactionDto);

        webTestClient.get().uri("/api/v1/payments/payout/list")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Transaction.class);

        Mockito.verify(transactionRepository, Mockito.times(1)).findByCreatedAt(any(), any());
    }

    @Test
    void getPayoutDetails() {
        Mockito.when(transactionRepository.findById("test_id")).thenReturn(Mono.just(transaction));
        Mockito.when(transactionMapper.map(transaction)).thenReturn(transactionDto);

        webTestClient.get().uri("/api/v1/payments/payout/{id}/details", "test_id")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.amount").isEqualTo(1000);

        Mockito.verify(transactionRepository, Mockito.times(1)).findById("test_id");
    }
}
