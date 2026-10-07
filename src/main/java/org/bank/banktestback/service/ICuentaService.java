package org.bank.banktestback.service;

import org.bank.banktestback.dto.CuentaDTO;

import java.util.List;

public interface ICuentaService {

    List<CuentaDTO> listarCuentas();
    List<CuentaDTO> listarPorCliente(String clienteId);
    CuentaDTO obtenerPorNumeroCuenta(Long numeroCuenta);
    CuentaDTO crear(CuentaDTO dto);
    CuentaDTO actualizar(Long numeroCuenta, CuentaDTO dto);
    void eliminar(Long numeroCuenta);
}
