package com.example.paymentprovider.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "cards")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Card {
    @Id
    private Long id;
    private Long cardNumber;
    private String expDate;
    private Integer cvv;
    @Transient
    @ToString.Exclude
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Customer customer;

}
