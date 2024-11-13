package com.pitang.desafio.tcepe.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pitang.desafio.tcepe.model.Car;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    @JsonIgnore
    @Schema(description = "id", example = "1")
    private Long id;
    @Schema(description = "nome", example = "Fulano")
    private String firstName;
    @Schema(description = "Sobrenome", example = "Ciclano")
    private String lastName;
    @Schema(description = "E-mail", example = "fulano@ciclano.com")
    private String email;
    @Schema(description = "Data Nascimento", example = "AAAA-MM-DD")
    private Date birthday;
    @Schema(description = "Login", example = "fulano")
    private String login;
    @Schema(description = "Password", example = "#$¨&*(")
    private String password;
    @Schema(description = "Password", example = "8199999-9999")
    private String phone;
    @Schema(description = "Lista de Carros")
    private List<Car> cars;
}
