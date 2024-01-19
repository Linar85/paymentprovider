package com.example.paymentprovider.mapper;

import com.example.paymentprovider.dto.WalletDto;
import com.example.paymentprovider.entity.Wallet;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WalletMapper {

    WalletDto map(Wallet entity);

    @InheritInverseConfiguration
    Wallet map(WalletDto dto);

}
