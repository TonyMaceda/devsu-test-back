package org.bank.banktestback.service.impl;

import lombok.RequiredArgsConstructor;
import org.bank.banktestback.dto.CuentaDTO;
import org.bank.banktestback.exception.CuentaConMovimientosException;
import org.bank.banktestback.exception.RecursoExistenteException;
import org.bank.banktestback.exception.RecursoNoEncontradoException;
import org.bank.banktestback.model.Cliente;
import org.bank.banktestback.model.Cuenta;
import org.bank.banktestback.repository.ClienteRepository;
import org.bank.banktestback.repository.CuentaRepository;
import org.bank.banktestback.service.ICuentaService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CuentaService implements ICuentaService {

    private final CuentaRepository cuentaRepository;
    private final ClienteRepository clienteRepository;

    @Override
    public List<CuentaDTO> listarCuentas() {
        return cuentaRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public List<CuentaDTO> listarPorCliente(String clienteId) {
        return cuentaRepository.findByCliente_ClienteId(clienteId).stream().map(this::toDTO).toList();
    }

    @Override
    public CuentaDTO obtenerPorNumeroCuenta(Long numeroCuenta) {
        return toDTO(obtenerCuentaPorNumeroCuenta(numeroCuenta));
    }

    @Override
    public CuentaDTO crear(CuentaDTO dto) {
        if (cuentaRepository.existsById(dto.numeroCuenta())) {
            throw new RecursoExistenteException("Ya existe una cuenta con número " + dto.numeroCuenta());
        }
        Cliente cliente = clienteRepository.findByClienteId(dto.clienteId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado: " + dto.clienteId()));

        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(dto.numeroCuenta());
        cuenta.setTipoCuenta(dto.tipoCuenta());
        cuenta.setSaldoInicial(dto.saldoInicial());
        cuenta.setEstado(dto.estado() != null ? dto.estado() : true);
        cuenta.setCliente(cliente);

        return toDTO(cuentaRepository.save(cuenta));
    }

    @Override
    public CuentaDTO actualizar(Long numeroCuenta, CuentaDTO dto) {
        Cuenta cuenta = obtenerCuentaPorNumeroCuenta(numeroCuenta);
        cuenta.setTipoCuenta(dto.tipoCuenta());
        cuenta.setEstado(dto.estado());
        return toDTO(cuentaRepository.save(cuenta));    }

    @Override
    public void eliminar(Long numeroCuenta) {
        Cuenta cuenta = obtenerCuentaPorNumeroCuenta(numeroCuenta);
        if (!cuenta.getMovimientos().isEmpty()) {
            throw new CuentaConMovimientosException("No se puede eliminar una cuenta con movimientos registrados.");
        }
        cuentaRepository.delete(cuenta);
    }

    private Cuenta obtenerCuentaPorNumeroCuenta(Long numeroCuenta) {
        return cuentaRepository.findById(numeroCuenta)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cuenta no encontrada: " + numeroCuenta));
    }

    private CuentaDTO toDTO(Cuenta c) {
        return CuentaDTO.builder()
                .numeroCuenta(c.getNumeroCuenta())
                .tipoCuenta(c.getTipoCuenta())
                .saldoInicial(c.getSaldoInicial())
                .estado(c.getEstado())
                .clienteId(c.getCliente().getClienteId())
                .build();
    }
}
