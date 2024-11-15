package com.pitang.desafio.tcepe.exception.expections;

import lombok.Getter;

@Getter
public class InvalidLoginPasswordException extends RuntimeException {
    private final int errorCode;

    public InvalidLoginPasswordException(final ErrorMessage e){
        this.errorCode = e.getErrorCode();
    }
}
