package com.pitang.desafio.tcepe.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.pitang.desafio.tcepe.dto.UserDTO;
import com.pitang.desafio.tcepe.model.User;
import com.pitang.desafio.tcepe.service.IUserService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

import static com.pitang.desafio.tcepe.dto.UserDTO.toMeDTO;

@RestController
@RequestMapping("/api")
public class MeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MeController.class);
    @Autowired
    private final IUserService service;

    public MeController(final IUserService service) {
        this.service = service;
    }

    @GetMapping("/me")
    @Operation(summary = "List logger user data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logged user data found"),
    })
    public ResponseEntity<UserDTO> getUserDetails(@AuthenticationPrincipal User user) {
        if (Objects.isNull(user)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.ok(toMeDTO(user));
    }
}
