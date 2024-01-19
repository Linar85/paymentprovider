package com.example.paymentprovider.repository;

import com.example.paymentprovider.entity.Webhook;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface WebhookRepository extends R2dbcRepository<Webhook, String> {
}
