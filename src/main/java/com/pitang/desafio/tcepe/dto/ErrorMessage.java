package com.pitang.desafio.tcepe.dto;

import lombok.Getter;

@Getter
public class ErrorMessage {
    private String error;
    private int errorCode;

    public ErrorMessage(String error, int errorCode) {
        this.error = error;
        this.errorCode = errorCode;
    }


}
