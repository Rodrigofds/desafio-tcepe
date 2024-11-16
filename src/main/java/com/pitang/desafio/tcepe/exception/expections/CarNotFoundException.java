package com.pitang.desafio.tcepe.exception.expections;

import lombok.Getter;

@Getter
public class CarNotFoundException extends RuntimeException {
    private final int errorCode;

    public CarNotFoundException(final ErrorMessage e){
        super(e.getError());
        this.errorCode = e.getErrorCode();
    }
}
