package org.bank.banktestback.service;

import org.bank.banktestback.dto.MovimientoDTO;

import java.util.List;

public interface IMovimientoService {

    List<MovimientoDTO> listarPorCuenta(Long numeroCuenta);
    MovimientoDTO obtenerPorId(Long id);
    MovimientoDTO registrarMovimiento(MovimientoDTO dto);
    MovimientoDTO actualizarMovimiento(Long id, MovimientoDTO dto);
    void eliminar(Long id);
}
