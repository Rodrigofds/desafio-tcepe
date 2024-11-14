package com.pitang.desafio.tcepe.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pitang.desafio.tcepe.exception.expections.ErrorMessage;
import com.pitang.desafio.tcepe.dto.UserDTO;
import com.pitang.desafio.tcepe.exception.expections.EmailException;
import com.pitang.desafio.tcepe.exception.expections.LoginException;
import com.pitang.desafio.tcepe.model.User;
import com.pitang.desafio.tcepe.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService {

    private final IUserRepository repository;

    @Autowired
    public UserServiceImpl(final IUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<UserDTO> findAllUsers() {
        ObjectMapper objectMapper = new ObjectMapper();
        final List<User> users = repository.findAll();

        return users.isEmpty()
                ? new ArrayList<>()
                : users
                .stream()
                .map(user -> objectMapper
                        .convertValue(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO createUser(@Valid final UserDTO userDTO) throws EmailException, LoginException  {

        userEmailValidation(userDTO.getEmail());
        userLoginValidation(userDTO.getLogin());
        User user = UserDTO.fromDTO(userDTO);

        try {
            repository.save(user);
            return UserDTO.toDTO(user);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void userEmailValidation(final String email) throws EmailException {
        final Optional<User> user = repository.findByEmail(email);

        if (user.isPresent()) {
            throw new EmailException(new ErrorMessage("Email already exists", 100));
        }
    }

    private void userLoginValidation(final String login) throws LoginException {
        final Optional<User> user = repository.findByLogin(login);

        if (user.isPresent()) {
            throw new LoginException(new ErrorMessage("Login already exists", 101));
        }
    }
}
