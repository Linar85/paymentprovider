package com.example.paymentprovider.dto;

import com.example.paymentprovider.entity.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public class TransactionDto {
    private String id;
    private String externalTransactionId;
    private TransactionType transactionType;
    private PaymentMethod paymentMethod;
    private Integer amount;
    private Currency currency;
    private LocalDateTime externalCreatedAt;
    private CardDto card;
    private Language languageTr;
    private String notificationUrl;
    private Status status;
    private String messageTr;
    private CustomerDto customer;
    private Long customerId;
    private LocalDateTime createdAt;
    //    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
//    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime updatedAt;


}
