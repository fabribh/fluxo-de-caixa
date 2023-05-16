package com.example.fluxodecaixa.api.controller;

import com.example.fluxodecaixa.api.mapper.TransactionRequestMapper;
import com.example.fluxodecaixa.api.mapper.TransactionResponseMapper;
import com.example.fluxodecaixa.api.mapper.TransactionResponseReportMapper;
import com.example.fluxodecaixa.api.model.TransactionRequest;
import com.example.fluxodecaixa.api.model.TransactionResponse;
import com.example.fluxodecaixa.api.model.TransactionResponseReport;
import com.example.fluxodecaixa.api.util.DateConverterUtil;
import com.example.fluxodecaixa.domain.model.OperationType;
import com.example.fluxodecaixa.domain.model.Transaction;
import com.example.fluxodecaixa.domain.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @Mock
    private TransactionResponseMapper responseMapper;

    @Mock
    private TransactionRequestMapper requestMapper;

    @Mock
    private TransactionResponseReportMapper responseReportMapper;

    @InjectMocks
    private TransactionController transactionController;

    @Test
    void itShouldCreateTransaction() {
        TransactionRequest request = TransactionRequest.builder()
                .operationTypeId(1)
                .amount(BigDecimal.TEN)
                .build();
        Transaction transaction = new Transaction();
        TransactionResponse transactionResponse = TransactionResponse.builder()
                .operationTypeId(OperationType.CREDIT.getValue())
                .amount(BigDecimal.TEN)
                .createdAt(OffsetDateTime.of(LocalDate.of(2023, 05, 15),
                        LocalTime.NOON, ZoneOffset.UTC))
                .build();

        given(requestMapper.apply(request)).willReturn(transaction);
        given(transactionService.createTransaction(transaction)).willReturn(transaction);
        given(responseMapper.apply(transaction)).willReturn(transactionResponse);

        TransactionResponse result = transactionController.createTransaction(request);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(transactionResponse);
    }

    @Test
    void itShouldFindAllBetweenDates() {
        LocalDate startDate = LocalDate.of(2023, 01, 10);
        LocalDate endDate = LocalDate.of(2023, 01, 11);

        OffsetDateTime startDateTime = DateConverterUtil.toOffsetDateTime(startDate, false);
        OffsetDateTime endDateTime = DateConverterUtil.toOffsetDateTime(endDate, true);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));

        Transaction transaction = Transaction.builder()
                .id(1L)
                .operationType(OperationType.CREDIT)
                .amount(new BigDecimal("500.00"))
                .createdAt(startDateTime)
                .build();

        TransactionResponse transactionResponse = TransactionResponse
                .builder()
                .operationTypeId("CREDIT")
                .amount(new BigDecimal("500.00"))
                .createdAt(startDateTime)
                .build();

        List<Transaction> transactions = Collections.singletonList(transaction);

        List<TransactionResponse> transactionResponseList = Collections.singletonList(transactionResponse);

        Page<Transaction> transactionPage = new PageImpl<>(transactions, pageable, 1);

        TransactionResponseReport transactionResponseReport = TransactionResponseReport.builder()
                .transactionResponses(transactionResponseList)
                .build();

        given(transactionService.findBetweenDates(startDateTime, endDateTime, pageable)).willReturn(transactionPage);
        given(responseMapper.apply(transaction)).willReturn(transactionResponse);
        given(responseReportMapper.apply(transactionResponseList)).willReturn(transactionResponseReport);

        var result = transactionController.findAllBetweenDates(startDate, endDate, pageable);

        assertThat(result).isNotNull();
        assertThat(result.transactionResponses()).isNotNull();
        assertThat(result.transactionResponses()).hasSize(1);
        assertThat(result.transactionResponses().get(0)).isEqualTo(transactionResponse);
    }
}