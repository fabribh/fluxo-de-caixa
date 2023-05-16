package com.example.fluxodecaixa.domain.service;

import com.example.fluxodecaixa.domain.model.OperationType;
import com.example.fluxodecaixa.domain.model.Transaction;
import com.example.fluxodecaixa.domain.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

class TransactionServiceTest {

    @Mock
    TransactionRepository repository;

    @Captor
    private ArgumentCaptor<Transaction> transactionArgumentCaptor;

    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transactionService = new TransactionService(repository);
    }

    @Test
    void itShouldCreateTransaction() {
        BigDecimal amount = new BigDecimal("500");
        Long id = 1L;

        Transaction transaction = Transaction
                .builder()
                .id(id)
                .operationType(OperationType.CREDIT)
                .amount(amount)
                .build();

        given(repository.save(transaction))
                .willReturn(transaction);

        transactionService.createTransaction(transaction);

        then(repository).should().save(transactionArgumentCaptor.capture());
        Transaction transactionArgumentCaptorValue = transactionArgumentCaptor.getValue();
        assertThat(transactionArgumentCaptorValue).isEqualTo(transaction);
    }

    @Test
    void itShouldFindBetweenDates() {
        OffsetDateTime startDate = OffsetDateTime.of(LocalDate.of(2023, 1, 10), LocalTime.NOON, ZoneOffset.UTC);
        OffsetDateTime endDate = OffsetDateTime.of(LocalDate.of(2023, 1, 10), LocalTime.NOON, ZoneOffset.UTC);

        PageRequest pageable = PageRequest.of(1, 10, Sort.by("id").ascending());
        BigDecimal amount = new BigDecimal("500");
        Long id = 1L;

        Transaction transactionOne = Transaction
                .builder()
                .id(id)
                .operationType(OperationType.CREDIT)
                .amount(amount)
                .createdAt(startDate)
                .build();
        Transaction transactionTwo = Transaction
                .builder()
                .id(id)
                .operationType(OperationType.CREDIT)
                .amount(amount)
                .createdAt(endDate)
                .build();
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transactionOne);
        transactions.add(transactionTwo);
        Page<Transaction> page = new PageImpl<>(transactions);

        given(repository.findAllByCreatedAtGreaterThanEqualAndCreatedAtIsLessThanEqual(
                any(OffsetDateTime.class), any(OffsetDateTime.class), any(Pageable.class)))
                .willReturn(page);

        Page<Transaction> response = transactionService.findBetweenDates(startDate, endDate, pageable);

        ArgumentCaptor<OffsetDateTime> startDateTimeCaptor = ArgumentCaptor.forClass(OffsetDateTime.class);
        ArgumentCaptor<OffsetDateTime> endDateTimeCaptor = ArgumentCaptor.forClass(OffsetDateTime.class);
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

        then(repository).should().findAllByCreatedAtGreaterThanEqualAndCreatedAtIsLessThanEqual(
                startDateTimeCaptor.capture(), endDateTimeCaptor.capture(), pageableCaptor.capture());

        OffsetDateTime capturedStartDate = startDateTimeCaptor.getValue();
        OffsetDateTime capturedEndDate = endDateTimeCaptor.getValue();
        Pageable capturedPageable = pageableCaptor.getValue();

        assertThat(capturedStartDate).isEqualTo(startDate);
        assertThat(capturedEndDate).isEqualTo(endDate);
        assertThat(capturedPageable).isEqualTo(pageable);
        assertThat(response.getTotalElements()).isEqualTo(2);
    }

    @Test
    void itShouldOptionalEmptyWhenNotFoundTransaction() {
        OffsetDateTime startDate = OffsetDateTime.of(LocalDate.of(2023, 1, 10), LocalTime.NOON, ZoneOffset.UTC);
        OffsetDateTime endDate = OffsetDateTime.of(LocalDate.of(2023, 1, 10), LocalTime.NOON, ZoneOffset.UTC);
        PageRequest pageable = PageRequest.of(1, 10, Sort.by("id").ascending());

        given(repository.findAllByCreatedAtGreaterThanEqualAndCreatedAtIsLessThanEqual(
                any(OffsetDateTime.class), any(OffsetDateTime.class), any(Pageable.class)))
                .willReturn(Page.empty());

        Page<Transaction> response = transactionService.findBetweenDates(startDate, endDate, pageable);

        ArgumentCaptor<OffsetDateTime> startDateTimeCaptor = ArgumentCaptor.forClass(OffsetDateTime.class);
        ArgumentCaptor<OffsetDateTime> endDateTimeCaptor = ArgumentCaptor.forClass(OffsetDateTime.class);
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

        then(repository).should().findAllByCreatedAtGreaterThanEqualAndCreatedAtIsLessThanEqual(
                startDateTimeCaptor.capture(), endDateTimeCaptor.capture(), pageableCaptor.capture());

        OffsetDateTime capturedStartDate = startDateTimeCaptor.getValue();
        OffsetDateTime capturedEndDate = endDateTimeCaptor.getValue();
        Pageable capturedPageable = pageableCaptor.getValue();

        assertThat(capturedStartDate).isEqualTo(startDate);
        assertThat(capturedEndDate).isEqualTo(endDate);
        assertThat(capturedPageable).isEqualTo(pageable);
        assertThat(response.getTotalElements()).isEqualTo(0);
    }
}