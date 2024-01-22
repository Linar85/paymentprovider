package com.example.paymentprovider.repository;

import com.example.paymentprovider.entity.Customer;
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

import static java.lang.String.format;

@DataR2dbcTest
@ExtendWith(SpringExtension.class)
@Testcontainers
@Disabled
class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

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
    void saveAndFind() {

        Customer customer = Customer.builder()
                .externalId(2L)
                .firstName("John")
                .lastName("Jones")
                .country("USA")
                .build();

        StepVerifier.create(customerRepository.save(customer)
                        .then(customerRepository.findByExternalId(2L)))
                .expectNextMatches(cs -> cs.getFirstName().equals(customer.getFirstName()) &&
                        cs.getLastName().equals(customer.getLastName()))
                .verifyComplete();
    }
}