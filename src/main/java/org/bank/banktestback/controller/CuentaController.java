package org.bank.banktestback.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bank.banktestback.dto.CuentaDTO;
import org.bank.banktestback.service.ICuentaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
@RequiredArgsConstructor
public class CuentaController {
    
    private final ICuentaService cuentaService;

    @GetMapping
    public ResponseEntity<List<CuentaDTO>> listar(@RequestParam(required = false) String clienteId) {
        List<CuentaDTO> list = (clienteId != null) ? cuentaService.listarPorCliente(clienteId) : cuentaService.listarCuentas();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuentaDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(cuentaService.obtenerPorNumeroCuenta(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CuentaDTO> crear(@Valid @RequestBody CuentaDTO dto) {
        return ResponseEntity.ok(cuentaService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CuentaDTO> actualizar(@PathVariable Long id, @Valid @RequestBody CuentaDTO dto) {
        return ResponseEntity.ok(cuentaService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        cuentaService.eliminar(id);
    }
}
