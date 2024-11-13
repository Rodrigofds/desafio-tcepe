package com.pitang.desafio_tcepe.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pitang.desafio_tcepe.dto.UserDTO;
import com.pitang.desafio_tcepe.model.User;
import com.pitang.desafio_tcepe.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService {

    private final IUserRepository repository;

    @Autowired
    public UserServiceImpl(IUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<List<UserDTO>> findAllUsers() {
        ObjectMapper objectMapper = new ObjectMapper();
        final List<User> users = repository.findAll();

        return users.isEmpty()
                ? Optional.empty()
                : Optional.of(users
                .stream()
                .map(user -> objectMapper
                        .convertValue(user, UserDTO.class))
                .collect(Collectors.toList()));
    }
}
