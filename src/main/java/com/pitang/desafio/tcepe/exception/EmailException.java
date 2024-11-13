package com.pitang.desafio.tcepe.exception;

import com.pitang.desafio.tcepe.dto.ErrorMessage;
import lombok.Getter;

@Getter
public class EmailException extends RuntimeException {
    private final int errorCode;

    public EmailException(final ErrorMessage e){
        this.errorCode = e.getErrorCode();
    }
}
