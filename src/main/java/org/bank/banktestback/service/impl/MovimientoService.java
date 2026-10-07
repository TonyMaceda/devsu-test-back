package org.bank.banktestback.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.bank.banktestback.dto.MovimientoDTO;
import org.bank.banktestback.enums.ETipoMovimiento;
import org.bank.banktestback.exception.OperacionNoPermitidaException;
import org.bank.banktestback.exception.RecursoNoEncontradoException;
import org.bank.banktestback.model.Cuenta;
import org.bank.banktestback.model.Movimiento;
import org.bank.banktestback.repository.CuentaRepository;
import org.bank.banktestback.repository.MovimientoRepository;
import org.bank.banktestback.service.IMovimientoService;
import org.bank.banktestback.service.movimiento.CreditoStrategy;
import org.bank.banktestback.service.movimiento.DebitoStrategy;
import org.bank.banktestback.service.movimiento.TipoMovimientoStrategy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovimientoService implements IMovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final CuentaRepository cuentaRepository;
    private final CreditoStrategy creditoStrategy;
    private final DebitoStrategy debitoStrategy;

    @Override
    public List<MovimientoDTO> listarPorCuenta(Long numeroCuenta) {
        obtenerCuenta(numeroCuenta);
        return movimientoRepository.findByCuenta_NumeroCuentaOrderByFechaAscIdAsc(numeroCuenta)
                .stream().map(this::toDTO).toList();
    }

    @Override
    public MovimientoDTO obtenerPorId(Long id) {
        return toDTO(obtenerMovimiento(id));
    }

    @Transactional
    @Override
    public MovimientoDTO registrarMovimiento(MovimientoDTO dto) {
        Cuenta cuenta = obtenerCuenta(dto.numeroCuenta());
        TipoMovimientoStrategy strategy = resolveStrategy(dto.tipoMovimiento());

        List<Movimiento> debitosDia = movimientoRepository.findDebitosDiaActual(cuenta.getNumeroCuenta(), LocalDate.now());
        strategy.validar(cuenta, dto.valor(), debitosDia);

        BigDecimal nuevoSaldo = strategy.calcularNuevoSaldo(cuenta.getSaldoInicial(), dto.valor());

        Movimiento movimiento = new Movimiento();
        movimiento.setFecha(LocalDate.now());
        movimiento.setTipoMovimiento(dto.tipoMovimiento());
        movimiento.setValor(dto.tipoMovimiento() == ETipoMovimiento.CREDITO ?
                dto.valor().abs() : dto.valor().abs().negate());
        movimiento.setSaldoDisponible(nuevoSaldo);
        movimiento.setCuenta(cuenta);

        cuenta.setSaldoInicial(nuevoSaldo);
        cuentaRepository.save(cuenta);

        return toDTO(movimientoRepository.save(movimiento));
    }

    @Override
    public MovimientoDTO actualizarMovimiento(Long id, MovimientoDTO dto) {
        throw new OperacionNoPermitidaException("Actualizar movimientos no está permitido. Registre un movimiento de reverso para corregir el error.");
    }

    @Transactional
    @Override
    public void eliminar(Long id) {
        Movimiento movimiento = obtenerMovimiento(id);
        Cuenta cuenta = movimiento.getCuenta();

        Movimiento ultimoMovimiento = movimientoRepository.findTopByCuenta_NumeroCuentaOrderByFechaDescIdDesc(cuenta.getNumeroCuenta())
                .orElseThrow(() -> new RecursoNoEncontradoException("La cuenta no tiene movimientos."));

        if (!ultimoMovimiento.getId().equals(movimiento.getId())) {
            throw new OperacionNoPermitidaException(
                    "Solo se puede eliminar el último movimiento registrado de la cuenta. " +
                    "Para corregir un movimiento anterior, registre un movimiento de reverso.");
        }

        BigDecimal saldoRevertido = cuenta.getSaldoInicial().subtract(movimiento.getValor());
        cuenta.setSaldoInicial(saldoRevertido);

        cuentaRepository.save(cuenta);
        movimientoRepository.delete(movimiento);
    }

    private Cuenta obtenerCuenta(Long numeroCuenta) {
        return cuentaRepository.findById(numeroCuenta)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cuenta no encontrada: " + numeroCuenta));
    }

    private Movimiento obtenerMovimiento(Long id) {
        return movimientoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Movimiento no encontrado con id: " + id));
    }

    private TipoMovimientoStrategy resolveStrategy(ETipoMovimiento tipoMovimiento) {
        return switch (tipoMovimiento) {
            case CREDITO -> creditoStrategy;
            case DEBITO -> debitoStrategy;
        };
    }

    private MovimientoDTO toDTO(Movimiento movimientoGuardado) {
        return MovimientoDTO.builder()
                .id(movimientoGuardado.getId())
                .fecha(movimientoGuardado.getFecha())
                .numeroCuenta(movimientoGuardado.getCuenta().getNumeroCuenta())
                .saldoDisponible(movimientoGuardado.getSaldoDisponible())
                .tipoMovimiento(movimientoGuardado.getTipoMovimiento())
                .valor(movimientoGuardado.getValor())
                .build();
    }
}
