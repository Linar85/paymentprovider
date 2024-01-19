package com.example.paymentprovider.service;

import com.example.paymentprovider.entity.Status;
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
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
@Slf4j
public class TopUpProcessingService {
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
    public Mono<Void> processingInProgressTopUpTransaction() {
        return transactionRepository.findByStatusAndTransactionType(Status.IN_PROGRESS, TransactionType.TOP_UP)
                .flatMap(transaction ->
                        walletRepository.getTopByCurrencyOrderByUpdatedAtDesc(transaction.getCurrency())
                                .flatMap(wallet -> {
                                    transaction.setStatus(STATUS_VALUES.get(RANDOM.nextInt(SIZE)));
//                                    transaction.setStatus(Status.FAILED);
                                    transaction.setUpdatedAt(LocalDateTime.now());
                                    if (transaction.getStatus().equals(Status.COMPLETED)) {
                                        wallet.setBalance(wallet.getBalance() + transaction.getAmount());
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
