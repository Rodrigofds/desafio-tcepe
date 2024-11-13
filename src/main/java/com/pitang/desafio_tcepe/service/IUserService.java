package com.pitang.desafio_tcepe.service;

import com.pitang.desafio_tcepe.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    Optional<List<UserDTO>> findAllUsers();
}
