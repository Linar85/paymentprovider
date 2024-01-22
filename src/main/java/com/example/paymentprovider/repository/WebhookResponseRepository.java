package com.example.paymentprovider.repository;

import com.example.paymentprovider.entity.WebhookResponse;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface WebhookResponseRepository extends R2dbcRepository<WebhookResponse, Long> {
}
