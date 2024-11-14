package com.pitang.desafio.tcepe.service;

import com.pitang.desafio.tcepe.dto.UserDTO;
import com.pitang.desafio.tcepe.exception.expections.EmailException;
import com.pitang.desafio.tcepe.exception.expections.LoginException;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    List<UserDTO> findAllUsers();
    UserDTO findUserById(Long id);

    UserDTO createUser(UserDTO userDTO) throws EmailException, LoginException;
}
