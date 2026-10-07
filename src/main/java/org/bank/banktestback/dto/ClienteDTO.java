package org.bank.banktestback.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ClienteDTO(
        Long id,
        @NotBlank(message = "El nombre es obligatorio.")
        String nombre,
        @NotBlank(message = "El género es obligatorio.")
        String genero,
        @NotNull(message = "La edad es obligatoria.")
        @Min(value = 18, message = "El cliente debe ser mayor de edad")
        Integer edad,
        @NotBlank(message = "La identificación es obligatoria.")
        String identificacion,
        @NotBlank(message = "La dirección es obligatoria.")
        String direccion,
        @NotBlank(message = "El teléfono es obligatorio.")
        String telefono,
        @NotBlank(message = "El clienteId es obligatorio.")
        String clienteId,
        @NotNull(message = "La contraseña es obligatoria.")
        String contrasena,
        Boolean estado
) {}
