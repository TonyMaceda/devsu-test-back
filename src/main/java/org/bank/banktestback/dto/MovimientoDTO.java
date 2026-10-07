package org.bank.banktestback.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.bank.banktestback.enums.ETipoMovimiento;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record MovimientoDTO(

    Long id,
    LocalDate fecha,
    @NotNull(message = "El tipo de movimiento es obligatorio.")
    ETipoMovimiento tipoMovimiento,
    @NotNull(message = "El valor es obligatorio.")
    @DecimalMin(value = "0.01", message = "El valor debe ser mayor a 0.")
    BigDecimal valor,
    BigDecimal saldoDisponible,
    @NotNull(message = "El número de cuenta es obligatorio.")
    Long numeroCuenta

    ) {}
