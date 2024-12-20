package com.pitang.desafio.tcepe.controller;

import com.pitang.desafio.tcepe.dto.CarDTO;
import com.pitang.desafio.tcepe.dto.CarResponseDTO;
import com.pitang.desafio.tcepe.exception.expections.CarNotFoundException;
import com.pitang.desafio.tcepe.exception.expections.ErrorMessage;
import com.pitang.desafio.tcepe.exception.expections.InvalidsFieldsException;
import com.pitang.desafio.tcepe.exception.expections.LicensePlateExistsException;
import com.pitang.desafio.tcepe.exception.expections.MissingFieldsException;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

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
                    return ResponseEntity.status(HttpStatus.OK).body(cars);
                }

                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(cars);
            } catch (RuntimeException e) {
                LOGGER.error("Unexpected error: {}", e.getMessage());

            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("/cars/{id}")
    @Operation(summary = "Find an car by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Unexpected error"),
    })
    public ResponseEntity<CarDTO> getCarById(@AuthenticationPrincipal final User user, @PathVariable final Long id) {
        if (Objects.isNull(user)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        final CarDTO dto = service.findCarByIdUser(user, id);
        if (Objects.isNull(dto)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/cars")
    @Operation(summary = "Register a new car for the logged user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Car successfully registered"),
            @ApiResponse(responseCode = "400", description = "Invalid or missing fields"),
            @ApiResponse(responseCode = "401", description = "Unauthorized or invalid session"),
            @ApiResponse(responseCode = "409", description = "License plate already exists"),
            @ApiResponse(responseCode = "500", description = "Unexpected error")
    })
    public ResponseEntity<CarResponseDTO> createCar(@AuthenticationPrincipal final User user,
                                                    @Valid @RequestBody final CarDTO carDTO) {
        if (Objects.isNull(user)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(CarResponseDTO.error(new ErrorMessage("Unauthorized", 1099)));
        } else {
            try {
                final CarDTO createdCar = service.createCarForUser(user, carDTO);

                return ResponseEntity.status(HttpStatus.CREATED).body(CarResponseDTO.success(createdCar));

            } catch (LicensePlateExistsException e) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(CarResponseDTO.error(new ErrorMessage(e.getMessage(), e.getErrorCode())));

            } catch (MissingFieldsException | InvalidsFieldsException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(CarResponseDTO.error(new ErrorMessage(e.getMessage(), 1080)));

            } catch (RuntimeException e) {
                LOGGER.error("Unexpected error: {}", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(CarResponseDTO.error(new ErrorMessage("Unexpected error", 108)));
            }
        }
    }

    @PutMapping("/cars/{id}")
    @Operation(summary = "Update a car for the logged user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid or missing fields"),
            @ApiResponse(responseCode = "401", description = "Unauthorized or invalid session"),
            @ApiResponse(responseCode = "409", description = "License plate already exists"),
            @ApiResponse(responseCode = "404", description = "Car not found"),
            @ApiResponse(responseCode = "500", description = "Unexpected error")
    })
    public ResponseEntity<CarResponseDTO> updateCarById(@AuthenticationPrincipal final User user,
                                                    @PathVariable final Long id,
                                                    @Valid @RequestBody final CarDTO carDTO) {
        if (Objects.isNull(user)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(CarResponseDTO.error(new ErrorMessage("Unauthorized", 1099)));
        } else {
            try {
                CarDTO updatedCar = service.updateCarById(user, id, carDTO);

                return ResponseEntity.ok(CarResponseDTO.success(updatedCar));

            } catch (LicensePlateExistsException e) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(CarResponseDTO.error(new ErrorMessage(e.getMessage(), e.getErrorCode())));

            } catch (MissingFieldsException | InvalidsFieldsException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(CarResponseDTO.error(new ErrorMessage(e.getMessage(), 1080)));

            } catch (CarNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(CarResponseDTO.error(new ErrorMessage(e.getMessage(), e.getErrorCode())));

            } catch (RuntimeException e) {
                LOGGER.error("Unexpected error: {}", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(CarResponseDTO.error(new ErrorMessage("Unexpected error", 108)));
            }
        }
    }

    @DeleteMapping("/cars/{id}")
    @Operation(summary = "Delete a car from the logged user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car successfully deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized or invalid session"),
            @ApiResponse(responseCode = "404", description = "Car not found"),
            @ApiResponse(responseCode = "500", description = "Unexpected error")
    })
    public ResponseEntity<CarResponseDTO> deleteCarByUser(@AuthenticationPrincipal final User user,
                                                          @PathVariable final Long id) {
        if (Objects.isNull(user)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(CarResponseDTO.error(new ErrorMessage("Unauthorized", 1099)));
        }

        try {
            service.deleteCarByUser(user, id);
            return ResponseEntity.noContent().build();

        } catch (CarNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(CarResponseDTO.error(new ErrorMessage(e.getMessage(), e.getErrorCode())));
        } catch (RuntimeException e) {
            LOGGER.error("Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CarResponseDTO.error(new ErrorMessage("Unexpected error", 108)));
        }
    }

}