package com.example.paymentprovider.dto;

import com.example.paymentprovider.entity.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebhookDto {

    private String id;
    private String providerTransactionId;
    private String externalTransactionId;
    private TransactionType transactionType;
    private PaymentMethod paymentMethod;
    private Integer amount;
    private Currency currency;
    private LocalDateTime externalCreatedAt;
    private Language languageTr;
    private Status status;
    private String messageTr;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Card card;
}
