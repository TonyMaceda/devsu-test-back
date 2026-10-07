package org.bank.banktestback.dto;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ErrorResponseDTO(String mensaje, int status, LocalDateTime timestamp) {
    public static ErrorResponseDTO of(String mensaje, HttpStatus status) {
        return new ErrorResponseDTO(mensaje, status.value(), LocalDateTime.now());
    }
}