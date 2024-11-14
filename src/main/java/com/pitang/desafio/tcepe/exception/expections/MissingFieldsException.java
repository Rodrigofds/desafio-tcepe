package com.pitang.desafio.tcepe.exception.expections;

import lombok.Getter;

@Getter
public class MissingFieldsException extends RuntimeException {
    private final int errorCode;

    public MissingFieldsException(final ErrorMessage e){
        super(e.getError());
        this.errorCode = e.getErrorCode();
    }
}
