package com.example.paymentprovider.service;

import com.example.paymentprovider.entity.Status;
import com.example.paymentprovider.entity.Transaction;
import com.example.paymentprovider.entity.TransactionType;
import com.example.paymentprovider.mapper.TransactionMapper;
import com.example.paymentprovider.mapper.WebhookMapper;
import com.example.paymentprovider.repository.TransactionRepository;
import com.example.paymentprovider.repository.WalletRepository;
import com.example.paymentprovider.repository.WebhookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayoutProcessingService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final WebhookMapper webhookMapper;
    private final WalletRepository walletRepository;
    private final WebClient webClient;
    private final WebhookRepository webhookRepository;

    private static final List<Status> STATUS_VALUES = Stream.of(Status.values()).filter(x -> !x.equals(Status.IN_PROGRESS)).toList();
    private static final int SIZE = STATUS_VALUES.size();
    private static final Random RANDOM = new Random();

    @Scheduled(initialDelay = 20 * 000, fixedRate = 1 * 10 * 1000)
    public Mono<Void> createPayoutTransaction() {
        return transactionRepository.findByStatusAndTransactionType(Status.IN_PROGRESS, TransactionType.PAY_OUT)
                .flatMap(transaction ->
                        walletRepository.getTopByCurrencyOrderByUpdatedAtDesc(transaction.getCurrency())
                                .flatMap(wallet -> {
                                    if ((wallet.getBalance() - transaction.getAmount()) >= 0) {
                                        transaction.setStatus(Status.COMPLETED);
                                        transaction.setUpdatedAt(LocalDateTime.now());
                                        wallet.setBalance(wallet.getBalance() - transaction.getAmount());
                                        wallet.setDelta(transaction.getAmount());
                                        wallet.setCurrency(transaction.getCurrency());
                                        wallet.setUpdatedAt(LocalDateTime.now());
                                        return walletRepository.save(wallet)
                                                .then(transactionRepository.save(transaction))
                                                .publishOn(Schedulers.boundedElastic())
                                                .doOnSuccess(tr ->
                                                        webClient.post()
                                                                .contentType(MediaType.APPLICATION_JSON)
                                                                .bodyValue(transactionMapper.map(transaction))
                                                                .retrieve()
                                                                .bodyToMono(String.class)
                                                                .retryWhen(Retry.max(3))
                                                                .subscribe())
                                                .doOnSuccess(x -> log.info("transaction " + x.getExternalTransactionId() + " " + x.getStatus()));
                                    } else {
                                        transaction.setStatus(Status.FAILED);
                                        transaction.setMessageTr("insufficient funds");
                                        transaction.setUpdatedAt(LocalDateTime.now());
                                        return transactionRepository.save(transaction)
                                                .publishOn(Schedulers.boundedElastic())
                                                .doOnSuccess(tr ->
                                                        webClient.post()
                                                                .contentType(MediaType.APPLICATION_JSON)
                                                                .bodyValue(webhookMapper.mapTransactionToWebhookDto(tr))
                                                                .retrieve()
                                                                .bodyToMono(String.class)
                                                                .retryWhen(Retry.max(3))
                                                                .subscribe())
                                                .doOnSuccess(x -> log.info("transaction " + x.getExternalTransactionId() + " " + x.getStatus()));
                                    }
                                })).then();
    }
}

