package org.bank.banktestback.exception;

import org.springframework.http.HttpStatus;

public class RecursoExistenteException extends BancoException {
    public RecursoExistenteException (String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
