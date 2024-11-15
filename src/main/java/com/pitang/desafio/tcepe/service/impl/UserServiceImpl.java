package com.pitang.desafio.tcepe.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pitang.desafio.tcepe.dto.UserDTO;
import com.pitang.desafio.tcepe.exception.expections.EmailException;
import com.pitang.desafio.tcepe.exception.expections.ErrorMessage;
import com.pitang.desafio.tcepe.exception.expections.LoginException;
import com.pitang.desafio.tcepe.model.User;
import com.pitang.desafio.tcepe.repository.IUserRepository;
import com.pitang.desafio.tcepe.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private final IUserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(final IUserRepository repository, final PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserDTO> findAllUsers() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            final List<User> users = repository.findAll();

            return users.isEmpty()
                    ? new ArrayList<>()
                    : users
                    .stream()
                    .map(user -> objectMapper
                            .convertValue(user, UserDTO.class))
                    .collect(Collectors.toList());
        } catch (RuntimeException e) {
            LOGGER.error("Error during all users search.");
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public UserDTO findUserById(final Long id) {
        try {
            LOGGER.info("Start user search");
            final Optional<User> user = repository.findById(id);

            return user.map(UserDTO::toDTO).orElse(null);

        } catch (RuntimeException e) {
            LOGGER.error("Error during the user search.");
            throw new RuntimeException(e.getMessage());
        }
    }

    @Transactional
    @Override
    public UserDTO createUser(@Valid final UserDTO userDTO) throws EmailException, LoginException {

        userEmailValidation(userDTO.getEmail());
        userLoginValidation(userDTO.getLogin());
        final User user = UserDTO.fromDTO(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

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

    @Transactional
    @Override
    public UserDTO updateUserById(final Long id, final UserDTO dtoIn) throws EmailException, LoginException {
        try {
            final User userToUpdate = UserDTO.toUpdateFromDTO(dtoIn, id);

            final User upToDated = repository.saveAndFlush(userToUpdate);

            return UserDTO.toDTO(upToDated);

        } catch (RuntimeException e) {
            LOGGER.error("Error during updating user.");
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Transactional
    @Override
    public void deleteUserById(final Long id) throws EmailException, LoginException {
        try {
            repository.deleteById(id);

        } catch (RuntimeException e) {
            LOGGER.error("Error during delete user.");
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
