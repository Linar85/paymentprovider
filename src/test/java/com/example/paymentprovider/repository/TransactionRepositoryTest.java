package com.example.paymentprovider.repository;

import com.example.paymentprovider.entity.Status;
import com.example.paymentprovider.entity.TransactionType;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.lifecycle.Startables;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.lang.String.format;

@DataR2dbcTest
@ExtendWith(SpringExtension.class)
@Testcontainers
@Disabled
class TransactionRepositoryTest {

    @Autowired
    TransactionRepository transactionRepository;

    @Container
    public static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("payment_provider")
            .withUsername("postgres")
            .withPassword("1234")
            .withInitScript("db/scripts/transactions.sql");

    @BeforeAll
    static void runContainer() {
        Startables.deepStart(postgreSQLContainer);
    }

    @DynamicPropertySource
    private static void setDatasourceProperties(DynamicPropertyRegistry registry) {

        registry.add("spring.r2dbc.url", () ->
                format("r2dbc:pool:postgresql://%s:%d/%s",
                        postgreSQLContainer.getHost(),
                        postgreSQLContainer.getFirstMappedPort(),
                        postgreSQLContainer.getDatabaseName()));
        registry.add("spring.r2dbc.username", postgreSQLContainer::getUsername);
        registry.add("spring.r2dbc.password", postgreSQLContainer::getPassword);
    }

    @Test
    void findByCreatedAt() {
        StepVerifier.create(transactionRepository.findByCreatedAt(LocalDate.of(2024, 1, 12), TransactionType.TOP_UP))
                .expectNextCount(2L)
                .verifyComplete();
    }

    @Test
    void findByStatusAndTransactionTypeInProgress() {
        StepVerifier.create(transactionRepository.findByStatusAndTransactionType(Status.IN_PROGRESS, TransactionType.TOP_UP))
                .expectNextCount(1L)
                .verifyComplete();
    }

    @Test
    void findByStatusAndTransactionTypeComplited() {
        StepVerifier.create(transactionRepository.findByStatusAndTransactionType(Status.COMPLETED, TransactionType.TOP_UP))
                .expectNextCount(1L)
                .verifyComplete();
    }

    @Test
    void findBetweenDates() {
        LocalDateTime startDate = LocalDateTime.of(2024, 1, 9, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 1, 12, 23, 59);
        StepVerifier.create(transactionRepository.findBetweenDates(startDate, endDate, TransactionType.TOP_UP))
                .expectNextCount(3L)
                .verifyComplete();
    }
}