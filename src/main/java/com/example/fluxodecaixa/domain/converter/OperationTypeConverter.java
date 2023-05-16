package com.example.fluxodecaixa.domain.converter;

import com.example.fluxodecaixa.domain.model.OperationType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class OperationTypeConverter implements AttributeConverter<OperationType, Integer> {
    @Override
    public Integer convertToDatabaseColumn(OperationType operationType) {
        if (operationType == null)
            return null;
        return operationType.getId();
    }

    @Override
    public OperationType convertToEntityAttribute(Integer id) {
        if (id == null)
            return null;
        return Stream.of(OperationType.values())
                .filter(op -> op.getId().equals(id))
                .findFirst()
                .orElseThrow(IllegalAccessError::new);
    }
}
