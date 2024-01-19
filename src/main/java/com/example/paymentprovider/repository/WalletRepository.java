package com.example.paymentprovider.repository;

import com.example.paymentprovider.entity.Currency;
import com.example.paymentprovider.entity.Wallet;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface WalletRepository extends R2dbcRepository<Wallet, Long> {
//    Mono<Wallet> getDistinctTopByWalletByCurrencyOOrderByUpdatedAtDesc(Currency currency);
    Mono<Wallet> getTopByCurrencyOrderByUpdatedAtDesc(Currency currency);
}
