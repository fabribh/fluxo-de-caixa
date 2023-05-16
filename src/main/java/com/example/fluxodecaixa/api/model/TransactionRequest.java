package com.example.fluxodecaixa.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record TransactionRequest(
  @Positive @NotNull(message = "Operation Type Id cannot be null") @JsonProperty("operation_type_id") Integer operationTypeId,
  @Positive @NotNull(message = "Amount cannot be null") BigDecimal amount
){}
