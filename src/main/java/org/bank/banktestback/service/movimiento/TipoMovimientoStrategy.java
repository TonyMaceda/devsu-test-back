package org.bank.banktestback.service.movimiento;

import org.bank.banktestback.model.Cuenta;
import org.bank.banktestback.model.Movimiento;

import java.math.BigDecimal;
import java.util.List;

public interface TipoMovimientoStrategy {

    BigDecimal calcularNuevoSaldo(BigDecimal saldoActual, BigDecimal valor);

    void validar(Cuenta cuenta, BigDecimal valor, List<Movimiento> movimientosDia);
}
