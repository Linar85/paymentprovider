package com.example.paymentprovider.dto;

import com.example.paymentprovider.entity.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebhookDto {

    private String id;
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
