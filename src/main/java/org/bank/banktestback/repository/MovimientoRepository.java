package org.bank.banktestback.repository;

import org.bank.banktestback.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    List<Movimiento> findByCuenta_NumeroCuentaAndFechaBetween(Long numeroCuenta, LocalDate inicio, LocalDate fin);

    List<Movimiento> findByCuenta_NumeroCuentaOrderByFechaAscIdAsc(Long numeroCuenta);

    @Query("SELECT m FROM Movimiento m WHERE m.cuenta.numeroCuenta = :numeroCuenta " +
            "AND m.fecha = :fecha AND m.tipoMovimiento = 'DEBITO'")
    List<Movimiento> findDebitosDiaActual(@Param("numeroCuenta") Long numeroCuenta, @Param("fecha") LocalDate fecha);

    Optional<Movimiento>findTopByCuenta_NumeroCuentaOrderByFechaDescIdDesc(Long numeroCuenta);
}
