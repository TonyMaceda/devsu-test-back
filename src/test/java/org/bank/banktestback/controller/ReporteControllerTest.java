package org.bank.banktestback.controller;

import org.bank.banktestback.dto.CuentaReporteDTO;
import org.bank.banktestback.dto.ReporteDTO;
import org.bank.banktestback.enums.ETipoCuenta;
import org.bank.banktestback.service.IReporteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ReporteControllerTest {

    @Mock
    private IReporteService reporteService;

    @InjectMocks
    private ReporteController reporteController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generarReporteFormatoJson() {
        ReporteDTO mockReporte = new ReporteDTO(
                "12345",
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 1, 31),
                List.of(
                        new CuentaReporteDTO(
                                123L,
                                ETipoCuenta.AHORROS,
                                BigDecimal.valueOf(1000),
                                BigDecimal.valueOf(500),
                                BigDecimal.valueOf(200)
                        )
                )
        );
        when(reporteService.generarReporte(anyString(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(mockReporte);

        ResponseEntity<?> response = reporteController.generarReporte(
                "12345",
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 1, 31),
                "json"
        );

        assertEquals(200, response.getStatusCode().value());
        assertEquals(mockReporte, response.getBody());
        verify(reporteService, times(1)).generarReporte(anyString(), any(LocalDate.class), any(LocalDate.class));
        verifyNoMoreInteractions(reporteService);
    }

    @Test
    void generarReporteFormatoPdf() {
        String mockPdfBase64 = "dGVzdC1wZGY=";
        ReporteDTO mockReporte = mock(ReporteDTO.class);
        when(reporteService.generarReporte(anyString(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(mockReporte);
        when(reporteService.generarPdfBase64(any(ReporteDTO.class)))
                .thenReturn(mockPdfBase64);

        ResponseEntity<?> response = reporteController.generarReporte(
                "12345",
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 1, 31),
                "pdf"
        );

        assertEquals(200, response.getStatusCode().value());
        assertEquals(Map.of("pdfBase64", mockPdfBase64), response.getBody());
        verify(reporteService, times(1)).generarReporte(anyString(), any(LocalDate.class), any(LocalDate.class));
        verify(reporteService, times(1)).generarPdfBase64(any(ReporteDTO.class));
        verifyNoMoreInteractions(reporteService);
    }
}