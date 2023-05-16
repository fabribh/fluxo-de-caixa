package com.example.fluxodecaixa.api.mapper;

import com.example.fluxodecaixa.api.model.TransactionResponse;
import com.example.fluxodecaixa.api.model.TransactionResponseReport;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

@Service
public class TransactionResponseReportMapper implements Function<List<TransactionResponse>, TransactionResponseReport> {

    @Override
    public TransactionResponseReport apply(List<TransactionResponse> transactions) {

        BigDecimal total = transactions
                .stream()
                .map(t -> t.amount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new TransactionResponseReport(transactions, total);
    }
}
