package com.pitang.desafio.tcepe.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pitang.desafio.tcepe.dto.UserDTO;
import com.pitang.desafio.tcepe.model.Car;
import com.pitang.desafio.tcepe.model.User;
import com.pitang.desafio.tcepe.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ObjectMapper objectMapper = new ObjectMapper();
    }

    @Test
    void deveDeveRetornarListaUsuariosVazia() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<UserDTO> result = userService.findAllUsers();

        assertTrue(result.isEmpty(), "Deve retornar result.empty() quando nenhum usuário é encontrado.");
    }

    @Test
    void deveDeveRetornarListaUsariosPreenchidaComSucesso() throws ParseException {
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
}