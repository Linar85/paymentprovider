package com.example.paymentprovider.repository;

import com.example.paymentprovider.entity.Customer;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CustomerRepository extends R2dbcRepository<Customer, Long> {
    Mono<Customer> findByExternalId(Long externalId);

    @Modifying
    @Query("insert into customers (external_id, first_name, last_name, country) values (:#{#customer.externalId}, :#{#customer.firstName}, :#{#customer.lastName}, :#{#customer.country}) on conflict DO NOTHING")
    Mono<Customer> save(final Customer customer);
}
