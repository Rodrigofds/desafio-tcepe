package com.pitang.desafio.tcepe.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pitang.desafio.tcepe.exception.expections.ErrorMessage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDTO {

    private UserDTO user;
    private ErrorMessage error;
    private String success;

    public static UserResponseDTO success(UserDTO user) {
        UserResponseDTO response = new UserResponseDTO();
        response.setUser(user);
        return response;
    }

    public static UserResponseDTO error(ErrorMessage error) {
        UserResponseDTO response = new UserResponseDTO();
        response.setError(error);
        return response;
    }

    public static UserResponseDTO success(String msg) {
        UserResponseDTO response = new UserResponseDTO();
        response.setSuccess(msg);
        return response;
    }
}
