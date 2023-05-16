package com.example.fluxodecaixa.api.mapper;

import com.example.fluxodecaixa.api.model.TransactionResponse;
import com.example.fluxodecaixa.domain.model.Transaction;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class TransactionResponseMapper implements Function<Transaction, TransactionResponse> {

    @Override
    public TransactionResponse apply(Transaction transaction) {
        return new TransactionResponse(transaction.getOperationType().getValue(),
                transaction.getAmount(), transaction.getCreatedAt());
    }
}
