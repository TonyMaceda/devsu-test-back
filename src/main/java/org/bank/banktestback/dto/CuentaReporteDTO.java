package org.bank.banktestback.dto;

import lombok.Builder;
import org.bank.banktestback.enums.ETipoCuenta;

import java.math.BigDecimal;

@Builder
public record CuentaReporteDTO(
        Long numeroCuenta,
        ETipoCuenta tipoCuenta,
        BigDecimal saldo,
        BigDecimal totalCreditos,
        BigDecimal totalDebitos
) {}
