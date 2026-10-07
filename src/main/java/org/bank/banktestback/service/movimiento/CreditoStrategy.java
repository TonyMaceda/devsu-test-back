package org.bank.banktestback.service.movimiento;

import org.bank.banktestback.model.Cuenta;
import org.bank.banktestback.model.Movimiento;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component

public class CreditoStrategy implements TipoMovimientoStrategy {

    @Override
    public BigDecimal calcularNuevoSaldo(BigDecimal saldoActual, BigDecimal valor) {
        return saldoActual.add(valor.abs());
    }

    @Override
    public void validar(Cuenta cuenta, BigDecimal valor, List<Movimiento> movimientosDia) {

    }
}
