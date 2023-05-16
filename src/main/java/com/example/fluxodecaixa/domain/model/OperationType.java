package com.example.fluxodecaixa.domain.model;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum OperationType {

    CREDIT(1, "Crédito", 1),
    DEBIT(2, "Débito", -1);

    private Integer id;
    private String value;
    private Integer multiplying;

    OperationType(Integer id, String value, Integer multiplying) {
        this.id = id;
        this.value = value;
        this.multiplying = multiplying;
    }

    public static OperationType getById(Integer id) {
        return Stream.of(values())
                .filter(op -> op.id.equals(id))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
