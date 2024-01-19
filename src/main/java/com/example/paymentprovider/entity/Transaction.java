package com.example.paymentprovider.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;


@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Transaction {

    @Id
    private String id;
    private String externalTransactionId;
    private TransactionType transactionType;
    private PaymentMethod paymentMethod;
    private Integer amount;
    private Currency currency;
    private LocalDateTime externalCreatedAt;
    private Language languageTr;
    private String notificationUrl;
    private Status status;
    private String messageTr;
    private Long customerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @Transient
    @ToString.Exclude
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Customer customer;
    @Transient
    @ToString.Exclude
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Card card;
}
