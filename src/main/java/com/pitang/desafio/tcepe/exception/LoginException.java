package com.pitang.desafio.tcepe.exception;

import com.pitang.desafio.tcepe.dto.ErrorMessage;
import lombok.Getter;

@Getter
public class LoginException extends RuntimeException {
    private final int errorCode;

    public LoginException(final ErrorMessage e){
        super(e.getError());
        this.errorCode = e.getErrorCode();
    }
}
