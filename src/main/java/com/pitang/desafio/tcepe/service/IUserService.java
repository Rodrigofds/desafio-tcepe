package com.pitang.desafio.tcepe.service;

import com.pitang.desafio.tcepe.dto.UserDTO;
import com.pitang.desafio.tcepe.exception.expections.EmailException;
import com.pitang.desafio.tcepe.exception.expections.LoginException;

import javax.transaction.Transactional;
import java.util.List;

public interface IUserService {

    List<UserDTO> findAllUsers();
    UserDTO findUserById(Long id);

    UserDTO createUser(UserDTO userDTO) throws EmailException, LoginException;

    @Transactional
    UserDTO updateUserById(Long id, UserDTO dtoIn) throws EmailException, LoginException;

    void deleteUserById(Long id) throws EmailException, LoginException;
}
