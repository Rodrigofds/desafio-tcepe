package com.pitang.desafio_tcepe.dto;

import com.pitang.desafio_tcepe.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CarDTO {
    private Long id;
    @Schema(description = "Ano do Veículo", example = "1977")
    private int year;
    @Schema(description = "Placa", example = "ADB-7B87")
    private String licensePlate;
    @Schema(description = "Modelo", example = "VW Fusca")
    private String model;
    @Schema(description = "Cor do Veículo", example = "Azul Claro")
    private String color;
    @Schema(description = "Proprietário")
    private User user;
}
