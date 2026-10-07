package org.bank.banktestback.exception;

import org.springframework.http.HttpStatus;

public class OperacionNoPermitidaException extends BancoException {

    public OperacionNoPermitidaException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
