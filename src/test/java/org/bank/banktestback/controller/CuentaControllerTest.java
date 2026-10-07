package org.bank.banktestback.controller;

import org.bank.banktestback.dto.CuentaDTO;
import org.bank.banktestback.enums.ETipoCuenta;
import org.bank.banktestback.service.ICuentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class CuentaControllerTest {

    @Mock
    private ICuentaService cuentaService;

    @InjectMocks
    private CuentaController cuentaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarCuentas() {
        List<CuentaDTO> mockCuentas = List.of(
                CuentaDTO.builder().numeroCuenta(1L).tipoCuenta(ETipoCuenta.AHORROS).saldoInicial(BigDecimal.valueOf(1000)).build(),
                CuentaDTO.builder().numeroCuenta(2L).tipoCuenta(ETipoCuenta.CORRIENTE).saldoInicial(BigDecimal.valueOf(2000)).build()
        );
        when(cuentaService.listarCuentas()).thenReturn(mockCuentas);

        ResponseEntity<List<CuentaDTO>> response = cuentaController.listar(null);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(mockCuentas, response.getBody());
        verify(cuentaService, times(1)).listarCuentas();
        verifyNoMoreInteractions(cuentaService);
    }

    @Test
    void obtenerCuentaPorId() {
        CuentaDTO mockCuenta = CuentaDTO.builder()
                .numeroCuenta(123L)
                .tipoCuenta(ETipoCuenta.CORRIENTE)
                .estado(true)
                .saldoInicial(new BigDecimal(1000))
                .build();
        when(cuentaService.obtenerPorNumeroCuenta(123L)).thenReturn(mockCuenta);

        ResponseEntity<?> response = cuentaController.obtener(123L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(mockCuenta, response.getBody());
        verify(cuentaService, times(1)).obtenerPorNumeroCuenta(123L);
        verifyNoMoreInteractions(cuentaService);
    }

    @Test
    void crearCuenta() {
        CuentaDTO mockCuenta = CuentaDTO.builder()
                .numeroCuenta(1L)
                .tipoCuenta(ETipoCuenta.AHORROS)
                .saldoInicial(BigDecimal.valueOf(1000))
                .build();
        when(cuentaService.crear(any(CuentaDTO.class))).thenReturn(mockCuenta);

        ResponseEntity<CuentaDTO> response = cuentaController.crear(mockCuenta);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(mockCuenta, response.getBody());
        verify(cuentaService, times(1)).crear(any(CuentaDTO.class));
        verifyNoMoreInteractions(cuentaService);
    }

    @Test
    void actualizarCuenta() {
        CuentaDTO mockCuenta = CuentaDTO.builder()
                .numeroCuenta(1L)
                .tipoCuenta(ETipoCuenta.AHORROS)
                .saldoInicial(BigDecimal.valueOf(1000))
                .build();
        when(cuentaService.actualizar(eq(1L), any(CuentaDTO.class))).thenReturn(mockCuenta);

        ResponseEntity<CuentaDTO> response = cuentaController.actualizar(1L, mockCuenta);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(mockCuenta, response.getBody());
        verify(cuentaService, times(1)).actualizar(eq(1L), any(CuentaDTO.class));
        verifyNoMoreInteractions(cuentaService);
    }

    @Test
    void eliminarCuenta() {
        doNothing().when(cuentaService).eliminar(1L);

        cuentaController.eliminar(1L);

        verify(cuentaService, times(1)).eliminar(1L);
        verifyNoMoreInteractions(cuentaService);
    }
}
