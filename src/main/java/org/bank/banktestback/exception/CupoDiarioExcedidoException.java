package org.bank.banktestback.exception;

import org.springframework.http.HttpStatus;

public class CupoDiarioExcedidoException extends BancoException {

    public CupoDiarioExcedidoException (String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
