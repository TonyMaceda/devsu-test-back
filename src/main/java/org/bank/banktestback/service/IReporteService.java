package org.bank.banktestback.service;

import org.bank.banktestback.dto.ReporteDTO;

import java.time.LocalDate;

public interface IReporteService {

    ReporteDTO generarReporte(String clienteId, LocalDate desde, LocalDate hasta);
    String generarPdfBase64(ReporteDTO dto);
}
