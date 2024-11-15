package com.pitang.desafio.tcepe.controller;

import com.pitang.desafio.tcepe.dto.UserDTO;
import com.pitang.desafio.tcepe.dto.UserResponseDTO;
import com.pitang.desafio.tcepe.exception.expections.EmailException;
import com.pitang.desafio.tcepe.exception.expections.ErrorMessage;
import com.pitang.desafio.tcepe.exception.expections.LoginException;
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

    @GetMapping(path = "/users")
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
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        try {
            UserDTO dto = service.findUserById(id);
            if (Objects.nonNull(dto)) {
                return ResponseEntity.status(HttpStatus.OK).body(dto);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (RuntimeException e) {
            LOGGER.error("Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(path = "/users")
    @Operation(summary = "Create an user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created"),
            @ApiResponse(responseCode = "400", description = "Email already exists"),
            @ApiResponse(responseCode = "400", description = "Login already exists"),
            @ApiResponse(responseCode = "500", description = "Unexpected error")
    })
    public ResponseEntity<UserResponseDTO> saveUser(@RequestBody @Valid UserDTO dtoIn) {
        try {
            UserDTO dtoOut = service.createUser(dtoIn);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(UserResponseDTO.success(dtoOut));

        } catch (Exception e) {
            ErrorMessage errorMessage;
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

            if (e instanceof EmailException) {
                LOGGER.error("E-mail alert: {}", e.getMessage());
                errorMessage = new ErrorMessage("Email already exists", 100);
                status = HttpStatus.BAD_REQUEST;

            } else if (e instanceof LoginException) {
                LOGGER.error("Login alert: {}", e.getMessage());
                errorMessage = new ErrorMessage("Login already exists", 101);
                status = HttpStatus.BAD_REQUEST;

            } else {
                LOGGER.error("Unexpected error: {}", e.getMessage());
                errorMessage = new ErrorMessage("Unexpected error", 103);
            }

            return ResponseEntity.status(status).body(UserResponseDTO.error(errorMessage));
        }
    }

    @PutMapping("/users/{id}")
    @Operation(summary = "Update an user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Unexpected error")
    })
    public ResponseEntity<UserDTO> updateAnUserById(@PathVariable Long id, @RequestBody @Valid UserDTO dtoIn) {
        try {
            UserDTO userToBeUpdate = service.findUserById(id);

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
    public ResponseEntity<UserResponseDTO> deleteUserById(@PathVariable Long id) {
        try {
            UserDTO dto = service.findUserById(id);

            if (Objects.nonNull(dto)) {
                service.deleteUserById(id);
                return ResponseEntity.status(HttpStatus.ACCEPTED)
                        .body(UserResponseDTO.success("Success: User " + id + " deleted"));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        } catch (RuntimeException e) {
            LOGGER.error("Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
