package org.bank.banktestback.exception;

import org.springframework.http.HttpStatus;

public class RecursoNoEncontradoException extends BancoException {

    public RecursoNoEncontradoException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
