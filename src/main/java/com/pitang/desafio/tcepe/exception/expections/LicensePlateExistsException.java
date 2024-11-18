package com.pitang.desafio.tcepe.exception.expections;

import lombok.Getter;

@Getter
public class LicensePlateExistsException extends RuntimeException {
    private final int errorCode;

    public LicensePlateExistsException(final ErrorMessage e){
        super(e.getError());
        this.errorCode = e.getErrorCode();
    }
}
