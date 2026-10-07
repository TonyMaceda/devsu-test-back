package org.bank.banktestback.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BancoException extends RuntimeException {

    private final HttpStatus status;

    public BancoException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
