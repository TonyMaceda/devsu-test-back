package org.bank.banktestback.exception;

import org.springframework.http.HttpStatus;

public class SaldoNoDisponibleException extends BancoException {

    public SaldoNoDisponibleException (String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
