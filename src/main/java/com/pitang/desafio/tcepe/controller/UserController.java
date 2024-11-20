package com.pitang.desafio.tcepe.controller;

import com.pitang.desafio.tcepe.dto.UserDTO;
import com.pitang.desafio.tcepe.dto.UserResponseDTO;
import com.pitang.desafio.tcepe.exception.expections.EmailException;
import com.pitang.desafio.tcepe.exception.expections.ErrorMessage;
import com.pitang.desafio.tcepe.exception.expections.LoginException;
import com.pitang.desafio.tcepe.exception.expections.UserNotFoundException;
import com.pitang.desafio.tcepe.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import java.util.NoSuchElementException;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private final IUserService service;

    public UserController(final IUserService service) {
        this.service = service;
    }

    @GetMapping("/users")
    @Operation(summary = "List all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users found"),
            @ApiResponse(responseCode = "204", description = "Users not found"),
    })
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        try {
            final List<UserDTO> users = service.findAllUsers();

            if (!users.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(users);
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(users);

        } catch (RuntimeException e) {
            LOGGER.error("Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/users/{id}")
    @Operation(summary = "Find an user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Unexpected error"),
    })
    public ResponseEntity<Object> getUserById(@PathVariable final Long id) {
        try {
            final UserDTO dto = service.findUserById(id);
            if (Objects.nonNull(dto)) {
                return ResponseEntity.status(HttpStatus.OK).body(dto);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (NoSuchElementException e) {
            LOGGER.error("User not found: {}", e.getMessage());
            ErrorMessage errorMessage = new ErrorMessage("User not found", 1845);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Invalid argument: {}", e.getMessage());
            ErrorMessage errorMessage = new ErrorMessage("Invalid argument", 1846);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        } catch (RuntimeException e) {
            LOGGER.error("Unexpected error: {}", e.getMessage());
            ErrorMessage errorMessage = new ErrorMessage("Unexpected error", 103);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    @PostMapping("/users")
    @Operation(summary = "Create an user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created"),
            @ApiResponse(responseCode = "400", description = "Email already exists"),
            @ApiResponse(responseCode = "400", description = "Login already exists"),
            @ApiResponse(responseCode = "500", description = "Unexpected error")
    })
    public ResponseEntity<UserResponseDTO> saveUser(@RequestBody @Valid final UserDTO dtoIn) {
        try {
            final UserDTO dtoOut = service.createUser(dtoIn);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(UserResponseDTO.success(dtoOut));

        } catch (EmailException e) {
            LOGGER.error("E-mail alert: {}", e.getMessage());
            ErrorMessage errorMessage = new ErrorMessage("Email already exists", 100);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(UserResponseDTO.error(errorMessage));

        } catch (LoginException e) {
            LOGGER.error("Login alert: {}", e.getMessage());
            ErrorMessage errorMessage = new ErrorMessage("Login already exists", 101);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(UserResponseDTO.error(errorMessage));

        } catch (RuntimeException e) {
            LOGGER.error("Unexpected error: {}", e.getMessage());
            ErrorMessage errorMessage = new ErrorMessage("Unexpected error", 103);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(UserResponseDTO.error(errorMessage));
        }
    }


    @PutMapping("/users/{id}")
    @Operation(summary = "Update an user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Unexpected error")
    })
    public ResponseEntity<UserDTO> updateAnUserById(@PathVariable final Long id,
                                                    @RequestBody @Valid final UserDTO dtoIn) {
        try {
            final UserDTO userToBeUpdate = service.findUserById(id);

            if (Objects.nonNull(userToBeUpdate)) {
                final UserDTO updated = service.updateUserById(id, dtoIn);
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(updated);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        } catch (RuntimeException e) {
            LOGGER.error("Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/users/{id}")
    @Operation(summary = "Delete an user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Unexpected error")
    })
    public ResponseEntity<UserResponseDTO> deleteUserById(@PathVariable final Long id) {
        try {
            final UserDTO dto = service.findUserById(id);

            if (Objects.nonNull(dto)) {
                service.deleteUserById(id);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        } catch (UserNotFoundException e) {
            LOGGER.error("User not found: {}", e.getMessage());
            ErrorMessage errorMessage = new ErrorMessage("User not found", 104);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(UserResponseDTO.error(errorMessage));

        } catch (RuntimeException e) {
            LOGGER.error("Unexpected error: {}", e.getMessage());
            ErrorMessage errorMessage = new ErrorMessage("Unexpected error", 103);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(UserResponseDTO.error(errorMessage));
        }
    }

}
