package com.example.paymentprovider.mapper;

import com.example.paymentprovider.dto.CustomerDto;
import com.example.paymentprovider.entity.Customer;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerDto map(Customer entity);

    @InheritInverseConfiguration
    Customer map(CustomerDto dto);

}
