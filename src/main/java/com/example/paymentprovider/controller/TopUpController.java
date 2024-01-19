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
@RequestMapping("/api/v1/payments/topup")
public class TopUpController {

    private final TransactionService transactionService;
    private final TransactionRsMapper transactionRsMapper;
    private final TransactionMapper transactionMapper;

    @PostMapping()
    public Mono<TransactionRsDto> createTopUpTransaction(@RequestBody TransactionDto transactionDto) {
        return transactionService.createTopUpTransaction(transactionMapper.map(transactionDto))
                .map(transactionRsMapper::map);
    }

    @GetMapping("/list")
    public Flux<TransactionDto> getTransactionsList(@RequestParam(name = "start_date", required = false) LocalDateTime startDate,
                                                    @RequestParam(name = "end_date", required = false) LocalDateTime endDate) {
        return transactionService.getTransactionList(startDate, endDate, TransactionType.TOP_UP).map(transactionMapper::map);
    }

    @GetMapping("/{id}/details")
    public Mono<TransactionDto> getTransactionDetails(@PathVariable String id) {
        return transactionService.getTransactionById(id).map(transactionMapper::map);
    }
}
