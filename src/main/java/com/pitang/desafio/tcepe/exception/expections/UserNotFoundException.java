package com.pitang.desafio.tcepe.exception.expections;

import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {
    private final int errorCode;

    public UserNotFoundException(final ErrorMessage e){
        super(e.getError());
        this.errorCode = e.getErrorCode();
    }
}
