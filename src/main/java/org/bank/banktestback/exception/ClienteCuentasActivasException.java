package org.bank.banktestback.exception;

import org.springframework.http.HttpStatus;

public class ClienteCuentasActivasException extends BancoException {

    public ClienteCuentasActivasException (String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
