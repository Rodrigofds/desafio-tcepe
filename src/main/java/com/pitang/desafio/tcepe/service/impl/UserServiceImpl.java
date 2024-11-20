package com.pitang.desafio.tcepe.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pitang.desafio.tcepe.dto.UserDTO;
import com.pitang.desafio.tcepe.exception.expections.EmailException;
import com.pitang.desafio.tcepe.exception.expections.ErrorMessage;
import com.pitang.desafio.tcepe.exception.expections.LoginException;
import com.pitang.desafio.tcepe.exception.expections.UserNotFoundException;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.pitang.desafio.tcepe.dto.UserDTO.fromDTO;
import static com.pitang.desafio.tcepe.dto.UserDTO.toDTO;

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
            LOGGER.info("Searching users list");
            final List<User> users = repository.findAll();

            LOGGER.info("Returning users lists");
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

            LOGGER.info("User search");
            return user.map(UserDTO::toDTO).orElse(null);

        } catch (RuntimeException e) {
            LOGGER.error("Error during the user search.");
            throw new RuntimeException(e.getMessage());
        }
    }

    @Transactional
    @Override
    public UserDTO createUser(@Valid final UserDTO userDTO) throws EmailException, LoginException {
        LOGGER.info("Start user creation");

        LOGGER.info("User email validation");
        userEmailValidation(userDTO.getEmail());

        LOGGER.info("User login validation");
        userLoginValidation(userDTO.getLogin());

        final User user = fromDTO(userDTO);

        LOGGER.info("Enconding password");
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setCreatedAt(new Date());

        try {
            repository.save(user);

            LOGGER.info("User created");
            return toDTO(user);

        } catch (Exception e) {
            LOGGER.info("");
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
        LOGGER.info("Start user update");
        User existingUser = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(new ErrorMessage("User not found", 4695)));

        LOGGER.info("Updating...");
        existingUser.setFirstName(dtoIn.getFirstName());
        existingUser.setLastName(dtoIn.getLastName());
        existingUser.setEmail(dtoIn.getEmail());
        existingUser.setLogin(dtoIn.getLogin());
        existingUser.setPassword(dtoIn.getPassword());
        existingUser.setBirthday(dtoIn.getBirthday());
        existingUser.setPhone(dtoIn.getPhone());

        LOGGER.info("User updated");
        return toDTO(repository.saveAndFlush(existingUser));
    }

    @Transactional
    @Override
    public void updateLastLoginByUser(final UserDTO dtoIn) throws EmailException, LoginException {
        LOGGER.info("Updating user last session login");
        this.repository
                .findById(dtoIn.getId())
                .ifPresent(user -> {
                            user.setLastLogin(new Date());
                            repository.save(user);
                        }
                );
    }

    @Transactional
    @Override
    public void deleteUserById(final Long id) throws EmailException, LoginException {
        try {
            LOGGER.info("Start user deleting");
            repository.deleteById(id);

        } catch (RuntimeException e) {
            LOGGER.error("Error during delete user.");
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
