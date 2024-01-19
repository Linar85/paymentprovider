package com.example.paymentprovider.repository;

import com.example.paymentprovider.entity.Merchant;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface MerchantRepository extends R2dbcRepository<Merchant, Long> {

    Mono<Merchant> findByUsername(String username);
}
