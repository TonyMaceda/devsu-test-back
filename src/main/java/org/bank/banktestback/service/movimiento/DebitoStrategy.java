package org.bank.banktestback.service.movimiento;

import org.bank.banktestback.exception.CupoDiarioExcedidoException;
import org.bank.banktestback.exception.SaldoNoDisponibleException;
import org.bank.banktestback.model.Cuenta;
import org.bank.banktestback.model.Movimiento;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DebitoStrategy implements TipoMovimientoStrategy {

    private static final BigDecimal LIMITE_DIARIO = new BigDecimal("1000");

    @Override
    public BigDecimal calcularNuevoSaldo(BigDecimal saldoActual, BigDecimal valor) {
        return saldoActual.subtract(valor.abs());
    }

    @Override
    public void validar(Cuenta cuenta, BigDecimal valor, List<Movimiento> movimientosDia) {
        if (cuenta.getSaldoInicial().compareTo(BigDecimal.ZERO) == 0) {
            throw new SaldoNoDisponibleException("Saldo no disponible.");
        }
        BigDecimal totalDebitado = movimientosDia.stream()
                .map(m -> m.getValor().abs())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (totalDebitado.add(valor.abs()).compareTo(LIMITE_DIARIO) > 0) {
            throw new CupoDiarioExcedidoException("Cupo diario permitido excedido.");
        }
    }
}
