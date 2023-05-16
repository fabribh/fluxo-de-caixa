package com.example.fluxodecaixa.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record TransactionResponseReport(
        @JsonProperty("transações") List<TransactionResponse> transactionResponses,
        @JsonProperty("total") BigDecimal amount
        ){}
