package com.pitang.desafio_tcepe.service;

import com.pitang.desafio_tcepe.dto.UserDTO;

import java.util.List;

public interface IUserService {

    List<UserDTO> findAllUsers();
}
