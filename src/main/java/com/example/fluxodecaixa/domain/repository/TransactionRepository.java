package com.example.fluxodecaixa.domain.repository;

import com.example.fluxodecaixa.domain.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Page<Transaction> findAllByCreatedAtGreaterThanEqualAndCreatedAtIsLessThanEqual(
            OffsetDateTime startDate, OffsetDateTime endDate, Pageable pageable
    );
}
