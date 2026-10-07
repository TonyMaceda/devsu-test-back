package org.bank.banktestback.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.bank.banktestback.enums.ETipoCuenta;

import java.math.BigDecimal;

@Builder
public record CuentaDTO (
        @NotNull(message = "El número de cuenta es obligatorio.")
        Long numeroCuenta,
        @NotNull(message = "El tipo de cuenta es obligatorio.")
        ETipoCuenta tipoCuenta,
        @NotNull(message = "El saldo inicial es obligatorio.")
        @DecimalMin(value = "0.0", message = "El saldo inicial no puede ser negativo")
        BigDecimal saldoInicial,
        Boolean estado,
        @NotBlank(message = "El clienteId es obligatorio.")
        String clienteId
){}
