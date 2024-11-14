package com.pitang.desafio.tcepe.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pitang.desafio.tcepe.model.Car;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarDTO {

    private Long id;

    @NotNull(message = "Year cannot be null")
    @NotBlank(message = "Year cannot be blank")
    @Schema(description = "Car's year", example = "1977")
    private int year;

    @NotNull(message = "License plate cannot be null")
    @NotBlank(message = "License plate cannot be blank")
    @Schema(description = "License plate", example = "ADB-7B87")
    private String licensePlate;

    @NotNull(message = "Model cannot be null")
    @NotBlank(message = "Model cannot be blank")
    @Schema(description = "Model", example = "VW Fusca")
    private String model;

    @NotNull(message = "Color cannot be null")
    @NotBlank(message = "Color cannot be blank")
    @Schema(description = "Car's Color", example = "Ligth blue")
    private String color;

    @Schema(description = "Owner")
    @JsonIgnore
    private UserDTO user;

    public static CarDTO toDTO(Car car) {
        CarDTO carDTO = new CarDTO();
        carDTO.setId(car.getId());
        carDTO.setYear(car.getYear());
        carDTO.setLicensePlate(car.getLicensePlate());
        carDTO.setModel(car.getModel());
        carDTO.setColor(car.getColor());
        return carDTO;
    }

    public static Car fromDTO(CarDTO carDTO) {
        Car car = new Car();
        car.setId(carDTO.getId());
        car.setYear(carDTO.getYear());
        car.setLicensePlate(carDTO.getLicensePlate());
        car.setModel(carDTO.getModel());
        car.setColor(carDTO.getColor());
        return car;
    }
}
