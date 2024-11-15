package com.pitang.desafio.tcepe.controller;

import com.pitang.desafio.tcepe.dto.CarDTO;
import com.pitang.desafio.tcepe.service.ICarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CarController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CarController.class);
    @Autowired
    private final ICarService service;

    public CarController(final ICarService service) {
        this.service = service;
    }

    @GetMapping(path = "/cars")
    @Operation(summary = "List all cars")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cars found"),
            @ApiResponse(responseCode = "204", description = "Cars not found"),
    })
    public ResponseEntity<List<CarDTO>> getAllCars() {
        try {
            final List<CarDTO> cars = service.findAllCars();

            if (!cars.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(cars);
            }

            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(cars);
        } catch (RuntimeException e) {
            LOGGER.error("Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
