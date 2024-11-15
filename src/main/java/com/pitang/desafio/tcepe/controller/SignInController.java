package com.pitang.desafio.tcepe.controller;

import com.pitang.desafio.tcepe.dto.LoginDTO;
import com.pitang.desafio.tcepe.dto.LoginResponseDTO;
import com.pitang.desafio.tcepe.exception.expections.ErrorMessage;
import com.pitang.desafio.tcepe.model.User;
import com.pitang.desafio.tcepe.security.ITokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class SignInController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SignInController.class);
    private final AuthenticationManager authenticationManager;
    private final ITokenService tokenService;

    @Autowired
    public SignInController(final AuthenticationManager authenticationManager, final ITokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/signin")
    @Operation(summary = "Returns a token whether user authenticated successfully to API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User logged successfully"),
            @ApiResponse(responseCode = "500", description = "Unexpected error")
    })
    public ResponseEntity<Object> userLogin(@RequestBody @Valid final LoginDTO dto) {
        try {
            final Authentication auth = this.authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(dto.getLogin(), dto.getPassword()));

            final String token = tokenService.generateToken((User) auth.getPrincipal());

            return ResponseEntity.ok(new LoginResponseDTO(token));

        } catch (RuntimeException e) {
            ErrorMessage errorMessage;
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

            if (e instanceof UsernameNotFoundException) {
                LOGGER.error("Login or Password alert: {}", e.getMessage());
                errorMessage = new ErrorMessage(e.getMessage(), 1011);
                status = HttpStatus.BAD_REQUEST;
            } else {
                LOGGER.error("Unexpected error: {}", e.getMessage());
                errorMessage = new ErrorMessage("Unexpected error", 103);
            }

            return ResponseEntity.status(status).body(errorMessage);
        }
    }
}