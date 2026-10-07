package org.bank.banktestback.dto;

import java.time.LocalDate;
import java.util.List;

public record ReporteDTO (
        String clienteId,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        List<CuentaReporteDTO> cuentas
){}
