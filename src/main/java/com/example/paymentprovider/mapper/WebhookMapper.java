package com.example.paymentprovider.mapper;

import com.example.paymentprovider.dto.WebhookDto;
import com.example.paymentprovider.entity.Transaction;
import com.example.paymentprovider.entity.Webhook;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WebhookMapper {

    @Mapping(target = "id", ignore = true)
    WebhookDto mapTransactionToWebhookDto(Transaction transaction);

    @Mapping(target = "id", ignore = true)
    Webhook mapTransactionToWebhook(Transaction transaction);

    WebhookDto map(Webhook entity);

    @InheritInverseConfiguration
    Webhook map(WebhookDto dto);
}
