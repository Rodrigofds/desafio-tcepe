package com.pitang.desafio.tcepe.controller;

import com.pitang.desafio.tcepe.dto.UserDTO;
import com.pitang.desafio.tcepe.exception.EmailException;
import com.pitang.desafio.tcepe.exception.LoginException;
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
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private final IUserService service;

    public UserController(final IUserService service) {
        this.service = service;
    }

    @GetMapping(path = "/users", produces = "application/json")
    @Operation(summary = "List all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users found"),

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

    @PostMapping(path = "/users", consumes = "application/json")
    @Operation(summary = "create an user to API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created"),

    })
    public ResponseEntity<UserDTO> saveUser(@RequestBody @Valid UserDTO dtoIn) throws EmailException, LoginException {
        try {
            UserDTO dtoOut = service.createUser(dtoIn);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(dtoOut);

        } catch (EmailException | LoginException e) {
            if (e instanceof EmailException) {
                LOGGER.error("E-mail alert: {}", e.getMessage());
            } else if (e instanceof LoginException) {
                LOGGER.error("Login alert: {}", e.getMessage());
            } else {
                LOGGER.error("Invalid fields: {}", e.getMessage());
            }
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<Profissional> atualizar(@PathVariable Long id, @RequestBody Profissional profissional) {
//        Optional<Profissional> profissionalDb = profissionalService.buscarPorId(id);
//
//        if (Objects.nonNull(profissionalDb)){
//            Profissional profissionalAtualizado = profissionalService.atualizar(profissional);
//            return ResponseEntity
//                    .status(HttpStatus.OK)
//                    .body(profissionalAtualizado);
//        }
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> excluir(@PathVariable Long id) {
//        Optional<Profissional> profissionalDb = profissionalService.buscarPorId(id);
//
//        if (Objects.nonNull(profissionalDb)){
//            profissionalService.excluir(id);
//            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Sucesso profissional excluído");
//        }
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//    }
}
