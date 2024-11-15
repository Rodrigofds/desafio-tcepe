package com.pitang.desafio.tcepe.exception.expections;

import lombok.Getter;

@Getter
public class EmailException extends RuntimeException {
    private final int errorCode;

    public EmailException(final ErrorMessage e){
        this.errorCode = e.getErrorCode();
    }
}
