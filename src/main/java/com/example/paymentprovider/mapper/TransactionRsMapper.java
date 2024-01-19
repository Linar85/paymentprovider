package com.example.paymentprovider.mapper;

import com.example.paymentprovider.dto.TransactionRsDto;
import com.example.paymentprovider.entity.Transaction;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionRsMapper {

    TransactionRsDto map(Transaction entity);

    @InheritInverseConfiguration
    Transaction map(TransactionRsDto dto);
}
