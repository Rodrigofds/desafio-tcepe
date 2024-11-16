package com.pitang.desafio.tcepe.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pitang.desafio.tcepe.exception.expections.ErrorMessage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CarResponseDTO {

    private CarDTO car;
    private ErrorMessage error;
    private String success;

    public static CarResponseDTO success(CarDTO car) {
        CarResponseDTO response = new CarResponseDTO();
        response.setCar(car);
        return response;
    }

    public static CarResponseDTO error(ErrorMessage error) {
        CarResponseDTO response = new CarResponseDTO();
        response.setError(error);
        return response;
    }

    public static CarResponseDTO success(String msg) {
        CarResponseDTO response = new CarResponseDTO();
        response.setSuccess(msg);
        return response;
    }
}
