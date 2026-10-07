package org.bank.banktestback.exception;

import org.springframework.http.HttpStatus;

public class CuentaConMovimientosException extends BancoException {

    public CuentaConMovimientosException (String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
