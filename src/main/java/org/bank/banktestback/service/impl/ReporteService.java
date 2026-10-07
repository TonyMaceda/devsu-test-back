package org.bank.banktestback.service.impl;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.bank.banktestback.dto.CuentaReporteDTO;
import org.bank.banktestback.dto.ReporteDTO;
import org.bank.banktestback.enums.ETipoMovimiento;
import org.bank.banktestback.model.Cuenta;
import org.bank.banktestback.model.Movimiento;
import org.bank.banktestback.repository.CuentaRepository;
import org.bank.banktestback.repository.MovimientoRepository;
import org.bank.banktestback.service.IReporteService;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReporteService implements IReporteService {

    private final CuentaRepository cuentaRepository;
    private final MovimientoRepository movimientoRepository;

    @Override
    public ReporteDTO generarReporte(String clienteId, LocalDate desde, LocalDate hasta) {
        List<Cuenta> cuentas = cuentaRepository.findByCliente_ClienteId(clienteId);

        List<CuentaReporteDTO> detalleCuentas = cuentas.stream().map(cuenta -> {
            List<Movimiento> movimientos = movimientoRepository
                    .findByCuenta_NumeroCuentaAndFechaBetween(cuenta.getNumeroCuenta(), desde, hasta);

            BigDecimal totalCreditos = movimientos.stream()
                    .filter(m -> m.getTipoMovimiento() == ETipoMovimiento.CREDITO)
                    .map(Movimiento::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalDebitos = movimientos.stream()
                    .filter(m -> m.getTipoMovimiento() == ETipoMovimiento.DEBITO)
                    .map(Movimiento::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);

            return CuentaReporteDTO.builder()
                    .numeroCuenta(cuenta.getNumeroCuenta())
                    .tipoCuenta(cuenta.getTipoCuenta())
                    .saldo(cuenta.getSaldoInicial())
                    .totalCreditos(totalCreditos)
                    .totalDebitos(totalDebitos)
                    .build();
        }).toList();

        return new ReporteDTO(clienteId, desde, hasta, detalleCuentas);
    }

    @Override
    public String generarPdfBase64(ReporteDTO reporte) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            document.open();

            document.add(new Paragraph("Estado de Cuenta — Cliente: " + reporte.clienteId()));
            document.add(new Paragraph("Periodo: " + reporte.fechaInicio() + " a " + reporte.fechaFin()));
            document.add(new Paragraph(" "));

            for (CuentaReporteDTO c : reporte.cuentas()) {
                document.add(new Paragraph(
                        "Cuenta %d (%s) — Saldo: %s | Créditos: %s | Débitos: %s"
                                .formatted(c.numeroCuenta(), c.tipoCuenta(), c.saldo(), c.totalCreditos(), c.totalDebitos())
                ));
            }

            document.close();
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (DocumentException | IOException e) {
            throw new RuntimeException("Error generando el PDF", e);
        }
    }
}
