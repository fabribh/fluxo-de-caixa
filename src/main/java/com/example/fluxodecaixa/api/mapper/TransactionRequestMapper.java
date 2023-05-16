package com.example.fluxodecaixa.api.mapper;

import com.example.fluxodecaixa.api.model.TransactionRequest;
import com.example.fluxodecaixa.domain.model.OperationType;
import com.example.fluxodecaixa.domain.model.Transaction;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class TransactionRequestMapper implements Function<TransactionRequest, Transaction> {

    @Override
    public Transaction apply(TransactionRequest transaction) {
        var id = transaction.operationTypeId();
        OperationType operationType;

        try {
            operationType = OperationType.getById(id);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException(e.getMessage());
        }

        return Transaction.builder()
                .operationType(operationType)
                .amount(transaction.amount())
                .build();
    }
}
