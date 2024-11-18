package com.pitang.desafio.tcepe.exception.expections;

import lombok.Getter;

@Getter
public class InvalidsFieldsException extends RuntimeException {
    private final int errorCode;

    public InvalidsFieldsException(final ErrorMessage e){
        super(e.getError());
        this.errorCode = e.getErrorCode();
    }
}
