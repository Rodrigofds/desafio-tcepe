package com.pitang.desafio.tcepe.exception.expections;

import lombok.Getter;

@Getter
public class EmailException extends RuntimeException {
    private final int errorCode;

    public EmailException(final ErrorMessage e){
        super(e.getError());
        this.errorCode = e.getErrorCode();
    }
}
