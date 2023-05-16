package com.example.fluxodecaixa.domain.model;

import com.example.fluxodecaixa.domain.converter.OperationTypeConverter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Transaction {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = OperationTypeConverter.class)
    private OperationType operationType;

    private BigDecimal amount;

    @CreationTimestamp
    private OffsetDateTime createdAt;
}
