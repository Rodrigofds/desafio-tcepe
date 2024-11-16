package com.pitang.desafio.tcepe.controller;

import com.pitang.desafio.tcepe.dto.CarDTO;
import com.pitang.desafio.tcepe.dto.UserDTO;
import com.pitang.desafio.tcepe.model.Car;
import com.pitang.desafio.tcepe.model.User;
import com.pitang.desafio.tcepe.service.ICarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CarController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CarController.class);
    @Autowired
    private final ICarService service;

    public CarController(final ICarService service) {
        this.service = service;
    }

    @GetMapping("/cars")
    @Operation(summary = "List all cars")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cars found"),
            @ApiResponse(responseCode = "204", description = "Cars not found"),
    })
    public ResponseEntity<List<CarDTO>> getAllCars(@AuthenticationPrincipal final User user) {
        if (Objects.isNull(user)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } else {
            try {
                final List<CarDTO> cars = service.findCarsByUser(user);

                if (!cars.isEmpty()) {
                    return ResponseEntity
                            .status(HttpStatus.OK)
                            .body(cars);
                }

                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(cars);
            } catch (RuntimeException e) {
                LOGGER.error("Unexpected error: {}", e.getMessage());

            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("/cars/{id}")
    @Operation(summary = "Find an user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Unexpected error"),
    })
    public ResponseEntity<CarDTO> getCarById(@AuthenticationPrincipal User user, @PathVariable Long id) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        CarDTO dto = service.findCarByUser(user, id);
        if (Objects.isNull(dto)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(dto);
    }
}