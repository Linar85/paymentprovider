package com.example.paymentprovider.dto;

import com.example.paymentprovider.entity.Currency;
import com.example.paymentprovider.entity.Merchant;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WalletDto {
    private Long id;
    private Double balance;
    private Double delta;
    private LocalDateTime updatedAt;
    private Currency currency;
    private Merchant merchant;
}
