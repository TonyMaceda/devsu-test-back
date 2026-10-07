package org.bank.banktestback.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bank.banktestback.dto.MovimientoDTO;
import org.bank.banktestback.service.IMovimientoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movimientos")
@RequiredArgsConstructor
public class MovimientoController {

    private final IMovimientoService movimientoService;

    @GetMapping
    public ResponseEntity<List<MovimientoDTO>> listarPorNumeroCuenta(@RequestParam Long numeroCuenta) {
        return ResponseEntity.ok(movimientoService.listarPorCuenta(numeroCuenta));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimientoDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(movimientoService.obtenerPorId(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<MovimientoDTO> crear(@Valid @RequestBody MovimientoDTO dto) {
        return ResponseEntity.ok(movimientoService.registrarMovimiento(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovimientoDTO> actualizar(@PathVariable Long id, @Valid @RequestBody MovimientoDTO dto) {
        return ResponseEntity.ok(movimientoService.actualizarMovimiento(id, dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        movimientoService.eliminar(id);
    }
}
