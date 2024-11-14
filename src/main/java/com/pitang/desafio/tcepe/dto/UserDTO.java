package com.pitang.desafio.tcepe.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pitang.desafio.tcepe.model.Car;
import com.pitang.desafio.tcepe.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @Schema(description = "id", example = "1")
    private Long id;

    @NotNull(message = "First name cannot be null")
    @NotBlank(message = "First name cannot be blank")
    @Size(min = 3, max = 15, message = "First name should be between 3 and 15 characters")
    @Schema(description = "nome", example = "Fulano")
    private String firstName;

    @NotNull(message = "Last name cannot be null")
    @NotBlank(message = "Last name cannot be blank")
    @Size(min = 3, max = 15, message = "Last name should be between 3 and 15 characters")
    @Schema(description = "Sobrenome", example = "Ciclano")
    private String lastName;

    @NotNull(message = "E-mail cannot be null")
    @Size(min = 3, max = 15, message = "E-mail should be between 3 and 15 characters")
    @Schema(description = "E-mail", example = "fulano@ciclano.com")
    private String email;

    @NotNull(message = "Birthday cannot be null")
    @Schema(description = "Data Nascimento", example = "AAAA-MM-DD")
    private Date birthday;

    @NotNull(message = "Login cannot be null")
    @NotBlank(message = "Login cannot be blank")
    @Size(min = 3, max = 12, message = "Login should be between 3 and 12 characters")
    @Schema(description = "Login", example = "fulano")
    private String login;

    @NotNull(message = "Password cannot be null")
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 3, max = 15, message = "Password should be between 3 and 15 characters")
    @Schema(description = "Password", example = "#$¨&*(")
    private String password;

    @NotNull(message = "Phone cannot be null")
    @NotBlank(message = "Phone cannot be blank")
    @Size(min = 8, message = "Phone should have min 12 characters")
    @Schema(description = "Password", example = "8199999-9999")
    private String phone;

    @Schema(description = "Lista de Carros")
    private List<CarDTO> cars;

    public static UserDTO toDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setBirthday(user.getBirthday());
        userDTO.setLogin(user.getLogin());
        userDTO.setPassword(user.getPassword());
        userDTO.setPhone(user.getPhone());

        if (user.getCars() != null) {
            userDTO.setCars(user.getCars().stream()
                    .map(CarDTO::toDTO)
                    .collect(Collectors.toList()));
        }

        return userDTO;
    }

    public static User fromDTO(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setBirthday(userDTO.getBirthday());
        user.setLogin(userDTO.getLogin());
        user.setPassword(userDTO.getPassword());
        user.setPhone(userDTO.getPhone());

        if (Objects.nonNull(userDTO.getCars())) {
            user.setCars(userDTO.getCars().stream()
                    .map(carDTO -> {
                        Car car = CarDTO.fromDTO(carDTO);
                        car.setUser(user);
                        return car;
                    }).collect(Collectors.toList()));
        }

        return user;
    }
}
