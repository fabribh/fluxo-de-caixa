package com.example.fluxodecaixa.domain.service;

import com.example.fluxodecaixa.domain.model.OperationType;
import com.example.fluxodecaixa.domain.model.Transaction;
import com.example.fluxodecaixa.domain.repository.TransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Page<Transaction> findBetweenDates(OffsetDateTime startDate, OffsetDateTime endDate, Pageable pageable) {
        return transactionRepository
                .findAllByCreatedAtGreaterThanEqualAndCreatedAtIsLessThanEqual(startDate, endDate, pageable);
    }

    public Transaction createTransaction(Transaction transaction) {
        var newAmount = calculateAmount(transaction.getOperationType(), transaction.getAmount());
        transaction.setAmount(newAmount);
        return transactionRepository.save(transaction);
    }

    private BigDecimal calculateAmount(OperationType type, BigDecimal amount) {
        return new BigDecimal(String.valueOf(
                amount.multiply(
                        BigDecimal.valueOf(type.getMultiplying()))));
    }
}
