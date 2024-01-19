package com.example.paymentprovider.dto;

import com.example.paymentprovider.entity.Currency;
import com.example.paymentprovider.entity.Merchant;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WalletDto {
    private Long id;
    private Double balance;
    private Double delta;
    private Currency currency;
    private Merchant merchant;
}
