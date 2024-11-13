package com.pitang.desafio.tcepe.controller;

import com.pitang.desafio.tcepe.dto.UserDTO;
import com.pitang.desafio.tcepe.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private final IUserService service;

    public UserController(final IUserService service) {
        this.service = service;
    }

    @GetMapping(produces = "application/json")
    @Operation(summary = "List all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuários encontratos"),

    })
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        final List<UserDTO> users = service.findAllUsers();

        if (!users.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(users);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(users);
    }
}
