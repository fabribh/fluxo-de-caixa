package com.example.fluxodecaixa.api.controller;

import com.example.fluxodecaixa.api.mapper.TransactionRequestMapper;
import com.example.fluxodecaixa.api.mapper.TransactionResponseMapper;
import com.example.fluxodecaixa.api.mapper.TransactionResponseReportMapper;
import com.example.fluxodecaixa.api.model.TransactionRequest;
import com.example.fluxodecaixa.api.model.TransactionResponse;
import com.example.fluxodecaixa.api.model.TransactionResponseReport;
import com.example.fluxodecaixa.api.util.DateConverterUtil;
import com.example.fluxodecaixa.domain.model.Transaction;
import com.example.fluxodecaixa.domain.service.TransactionService;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    private final TransactionResponseMapper responseMapper;

    private final TransactionRequestMapper requestMapper;

    private final TransactionResponseReportMapper responseReportMapper;

    public TransactionController(TransactionService transactionService,
                                 TransactionResponseMapper responseMapper,
                                 TransactionRequestMapper requestMapper, TransactionResponseReportMapper responseReportMapper) {
        this.transactionService = transactionService;
        this.responseMapper = responseMapper;
        this.requestMapper = requestMapper;
        this.responseReportMapper = responseReportMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse createTransaction(@Validated @RequestBody TransactionRequest request) {
        var transaction = requestMapper.apply(request);
        var response = responseMapper.apply(transactionService.createTransaction(transaction));
        return response;
    }

    @GetMapping
    public TransactionResponseReport findAllBetweenDates(
            @Validated @NotNull(message = "StartDate cannot be null") @RequestParam
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @Validated @NotNull(message = "EndDate cannot be null") @RequestParam
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @Validated @PageableDefault(page = 1, size = 10, sort = {"id"})Pageable pageable) {

        var startDateOffsetDateTime = DateConverterUtil.toOffsetDateTime(startDate, false);
        var endDateOffsetDateTime = DateConverterUtil.toOffsetDateTime(endDate, true);

        Page<Transaction> transactions = transactionService
                .findBetweenDates(startDateOffsetDateTime, endDateOffsetDateTime, pageable);

        List<TransactionResponse> transactionResponseList = new ArrayList<>();
        transactions
                .stream()
                .forEach(t -> transactionResponseList.add(responseMapper.apply(t)));

        return responseReportMapper.apply(transactionResponseList);
    }
}
