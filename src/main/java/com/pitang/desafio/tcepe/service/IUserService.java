package com.pitang.desafio.tcepe.service;

import com.pitang.desafio.tcepe.dto.UserDTO;

import java.util.List;

public interface IUserService {

    List<UserDTO> findAllUsers();
}