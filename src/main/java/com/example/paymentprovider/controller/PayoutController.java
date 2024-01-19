package com.example.paymentprovider.controller;

import com.example.paymentprovider.dto.TransactionDto;
import com.example.paymentprovider.dto.TransactionRsDto;
import com.example.paymentprovider.entity.TransactionType;
import com.example.paymentprovider.mapper.TransactionMapper;
import com.example.paymentprovider.mapper.TransactionRsMapper;
import com.example.paymentprovider.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments/payout")
public class PayoutController {

    private final TransactionRsMapper transactionRsMapper;
    private final TransactionMapper transactionMapper;
    private final TransactionService transactionService;

    @PostMapping()
    public Mono<TransactionRsDto> createPayoutTransaction(@RequestBody TransactionDto transactionDto) {
        return transactionService.createTopUpTransaction(transactionMapper.map(transactionDto))
                .map(transactionRsMapper::map);
    }

    @GetMapping("/list")
    public Flux<TransactionDto> getPayoutsList(@RequestParam(required = false) LocalDateTime startDate,
                                               @RequestParam(required = false) LocalDateTime endDate) {
        return transactionService.getTransactionList(startDate, endDate, TransactionType.PAY_OUT).map(transactionMapper::map);
    }

    @GetMapping("/{id}/details")
    public Mono<TransactionDto> getPayoutDetails(@PathVariable String id) {
        return transactionService.getTransactionById(id).map(transactionMapper::map);
    }
}
