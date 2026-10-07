package org.bank.banktestback.controller;

import lombok.RequiredArgsConstructor;
import org.bank.banktestback.dto.ReporteDTO;
import org.bank.banktestback.service.IReporteService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final IReporteService reporteService;

    // GET /api/reportes?clienteId=X&fechaInicio=2026-01-01&fechaFin=2026-01-31&formato=json|pdf
    @GetMapping
    public ResponseEntity<?> generarReporte(
            @RequestParam String clienteId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(defaultValue = "json") String formato) {

        ReporteDTO reporte = reporteService.generarReporte(clienteId, fechaInicio, fechaFin);

        if ("pdf".equalsIgnoreCase(formato)) {
            String pdfBase64 = reporteService.generarPdfBase64(reporte);
            return ResponseEntity.ok(Map.of("pdfBase64", pdfBase64));
        }
        return ResponseEntity.ok(reporte);
    }
}
