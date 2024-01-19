package com.example.paymentprovider.mapper;

import com.example.paymentprovider.dto.TransactionDto;
import com.example.paymentprovider.entity.Transaction;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    TransactionDto map(Transaction entity);

    @InheritInverseConfiguration
    Transaction map(TransactionDto dto);

}
