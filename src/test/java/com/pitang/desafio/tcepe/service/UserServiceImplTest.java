package com.pitang.desafio.tcepe.service;

import com.pitang.desafio.tcepe.dto.UserDTO;
import com.pitang.desafio.tcepe.exception.expections.EmailException;
import com.pitang.desafio.tcepe.exception.expections.LoginException;
import com.pitang.desafio.tcepe.model.Car;
import com.pitang.desafio.tcepe.model.User;
import com.pitang.desafio.tcepe.repository.IUserRepository;
import com.pitang.desafio.tcepe.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() throws ParseException {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnAnEmptyList() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<UserDTO> result = userService.findAllUsers();

        assertTrue(result.isEmpty(), "Deve retornar result.empty() quando nenhum usuário é encontrado.");
    }

    @Test
    void shouldReturnAListOfAllUsersWithSuccess() throws ParseException {
        // Given
        List<Car> carros1 = new ArrayList<>();
        List<Car> carros2 = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDay = dateFormat.parse("1980-01-20");
        User user1 = new User(1L, "João", "Paulo da Silva", "paulo.joao@email.com", birthDay, "jp.silva", "123", "999999", carros1);
        User user2 = new User(2L, "Maria", "Antonia da Silva", "m.antonia@email.com", birthDay, "m.tonia", "234", "988888", carros2);

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        // When
        List<UserDTO> result = userService.findAllUsers();

        // Then
        assertEquals(2, result.size(), "Deve conter dois UserDTO na lista.");
    }

    @Test
    void shouldReturnAnUserIfEmailAndLoginValids() throws ParseException {
        UserDTO userDTO = getUserDTO();

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByLogin(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn(anyString());

        UserDTO createdUser = userService.createUser(userDTO);

        assertNotNull(createdUser);
        assertEquals("paulo.joao@email.com", createdUser.getEmail());
    }

    @Test
    void shouldThrowEmailExceptionIfEmailAlreadyExists() throws ParseException {
        UserDTO userDTO = getUserDTO();

        User existingUser = new User();
        existingUser.setEmail("paulo.joao@email.com");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(existingUser));

        assertThrows(EmailException.class, () -> userService.createUser(userDTO));
    }

    @Test
    void shouldThrowLoginExceptionIfLoginAlreadyExists() throws ParseException {
        UserDTO userDTO = getUserDTO();

        User existingUser = new User();
        existingUser.setLogin("jp.silva");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByLogin(anyString())).thenReturn(Optional.of(existingUser));

        assertThrows(LoginException.class, () -> userService.createUser(userDTO));
    }

    @Test
    void shouldThrowRuntimeException() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("valid@example.com");
        userDTO.setLogin("validUser");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByLogin(anyString())).thenReturn(Optional.empty());
        doThrow(new RuntimeException("Database error")).when(userRepository).save(any());

        assertThrows(RuntimeException.class, () -> userService.createUser(userDTO));
    }

    private static UserDTO getUserDTO() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDay = dateFormat.parse("1980-01-20");
        UserDTO userDTO = new UserDTO();
        userDTO.setId(null);
        userDTO.setFirstName("João");
        userDTO.setLastName("Paulo da Silva");
        userDTO.setBirthday(birthDay);
        userDTO.setEmail("paulo.joao@email.com");
        userDTO.setLogin("jp.silva");
        userDTO.setPassword("15464654");
        userDTO.setPhone("819999-9999");
        return userDTO;
    }
}