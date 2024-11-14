package com.pitang.desafio.tcepe.exception.expections;

import lombok.Getter;

@Getter
public class LoginException extends RuntimeException {
    private final int errorCode;

    public LoginException(final ErrorMessage e){
        super(e.getError());
        this.errorCode = e.getErrorCode();
    }
}
