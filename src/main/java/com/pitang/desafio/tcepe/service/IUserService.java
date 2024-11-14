package com.pitang.desafio.tcepe.service;

import com.pitang.desafio.tcepe.dto.UserDTO;
import com.pitang.desafio.tcepe.exception.expections.EmailException;
import com.pitang.desafio.tcepe.exception.expections.LoginException;

import java.util.List;

public interface IUserService {

    List<UserDTO> findAllUsers();

    UserDTO createUser(UserDTO userDTO) throws EmailException, LoginException;
}
