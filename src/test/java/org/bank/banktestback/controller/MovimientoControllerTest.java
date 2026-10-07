package org.bank.banktestback.controller;

import org.bank.banktestback.dto.MovimientoDTO;
import org.bank.banktestback.enums.ETipoMovimiento;
import org.bank.banktestback.exception.OperacionNoPermitidaException;
import org.bank.banktestback.service.IMovimientoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MovimientoControllerTest {

    @Mock
    private IMovimientoService movimientoService;

    @InjectMocks
    private MovimientoController movimientoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarMovimientos() {
        List<MovimientoDTO> mockMovimientos = List.of(
                MovimientoDTO.builder()
                        .id(1L)
                        .tipoMovimiento(ETipoMovimiento.CREDITO)
                        .valor(new BigDecimal(500))
                        .fecha(LocalDate.now())
                        .numeroCuenta(123L)
                        .build(),
                MovimientoDTO.builder()
                        .id(2L)
                        .tipoMovimiento(ETipoMovimiento.DEBITO)
                        .valor(new BigDecimal(300))
                        .fecha(LocalDate.now())
                        .numeroCuenta(123L)
                        .build()
        );
        when(movimientoService.listarPorCuenta(123L)).thenReturn(mockMovimientos);

        ResponseEntity<List<MovimientoDTO>> response = movimientoController.listarPorNumeroCuenta(123L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(mockMovimientos, response.getBody());
        verify(movimientoService, times(1)).listarPorCuenta(123L);
        verifyNoMoreInteractions(movimientoService);
    }

    @Test
    void obtenerMovimientoPorId() {
        MovimientoDTO mockMovimiento = MovimientoDTO.builder()
                .id(1L)
                .tipoMovimiento(ETipoMovimiento.CREDITO)
                .valor(new BigDecimal(500))
                .fecha(LocalDate.now())
                .build();
        when(movimientoService.obtenerPorId(1L)).thenReturn(mockMovimiento);

        ResponseEntity<MovimientoDTO> response = movimientoController.obtener(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(mockMovimiento, response.getBody());
        verify(movimientoService, times(1)).obtenerPorId(1L);
        verifyNoMoreInteractions(movimientoService);
    }

    @Test
    void realizarTransaccion() {
        MovimientoDTO mockMovimiento = MovimientoDTO.builder()
                .id(1L)
                .tipoMovimiento(ETipoMovimiento.CREDITO)
                .valor(new BigDecimal(500))
                .fecha(LocalDate.now())
                .build();

        when(movimientoService.registrarMovimiento(any(MovimientoDTO.class))).thenReturn(mockMovimiento);

        ResponseEntity<MovimientoDTO> response = movimientoController.crear(mockMovimiento);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(mockMovimiento, response.getBody());
        verify(movimientoService, times(1)).registrarMovimiento(any(MovimientoDTO.class));
        verifyNoMoreInteractions(movimientoService);
    }

    @Test
    void actualizarMovimiento() {
        MovimientoDTO mockMovimiento = MovimientoDTO.builder()
                .id(1L)
                .tipoMovimiento(ETipoMovimiento.CREDITO)
                .valor(new BigDecimal(500))
                .fecha(LocalDate.now())
                .build();
        when(movimientoService.actualizarMovimiento(eq(1L), any(MovimientoDTO.class))).thenThrow(
                new OperacionNoPermitidaException("Actualizar movimientos no está permitido. Registre un movimiento de reverso para corregir el error.")
        );

        assertThrows(OperacionNoPermitidaException.class, () -> {
            movimientoController.actualizar(1L, mockMovimiento);
        });
        verify(movimientoService, times(1)).actualizarMovimiento(eq(1L), any(MovimientoDTO.class));
        verifyNoMoreInteractions(movimientoService);
    }

    @Test
    void eliminarMovimiento() {
        doNothing().when(movimientoService).eliminar(1L);

        movimientoController.eliminar(1L);

        verify(movimientoService, times(1)).eliminar(1L);
        verifyNoMoreInteractions(movimientoService);
    }
}
