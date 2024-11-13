package com.pitang.desafio.tcepe.dto;

import com.pitang.desafio.tcepe.model.User;
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
    private User user;
}
