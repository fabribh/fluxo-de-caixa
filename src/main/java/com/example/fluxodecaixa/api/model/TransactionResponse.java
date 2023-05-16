package com.example.fluxodecaixa.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Builder
public record TransactionResponse(
        @JsonProperty("operation_type") String operationTypeId,
        BigDecimal amount,
        @JsonProperty("created_at") OffsetDateTime createdAt
        ){}
