package com.pitang.desafio_tcepe.controller;

import com.pitang.desafio_tcepe.dto.UserDTO;
import com.pitang.desafio_tcepe.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private final IUserService service;

    public UserController(final IUserService service) {
        this.service = service;
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<Optional<List<UserDTO>>> getAllUsers() {
        final Optional<List<UserDTO>> users = service.findAllUsers();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(users);
    }
}
