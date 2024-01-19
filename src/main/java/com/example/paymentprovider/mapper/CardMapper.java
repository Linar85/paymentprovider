package com.example.paymentprovider.mapper;

import com.example.paymentprovider.dto.CardDto;
import com.example.paymentprovider.entity.Card;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CardMapper {

    CardDto map(Card entity);

    @InheritInverseConfiguration
    Card map(CardDto dto);

}
