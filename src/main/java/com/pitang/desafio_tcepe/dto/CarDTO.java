package com.pitang.desafio_tcepe.dto;

import com.pitang.desafio_tcepe.model.User;
import lombok.Data;

@Data
public class CarDTO {
    private Long id;
    private int year;
    private String licensePlate;
    private String model;
    private String color;
    private User user;
}
