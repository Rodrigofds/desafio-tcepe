package com.pitang.desafio_tcepe.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pitang.desafio_tcepe.dto.UserDTO;
import com.pitang.desafio_tcepe.model.User;
import com.pitang.desafio_tcepe.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    void deveDeveRetornarListaUsuariosVazia() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        Optional<List<UserDTO>> result = userService.findAllUsers();

        assertTrue(result.isEmpty(), "Deve retornar Optional.empty() quando nenhum usuário é encontrado.");
    }

    @Test
    void deveDeveRetornarListaUsariosPreenchidaComSucesso() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDay = dateFormat.parse("1980-01-20");
        User user1 = new User("João", "Paulo da Silva", "paulo.joao@email.com", birthDay, "jp.silva", "123", "999999");
        User user2 = new User("Maria", "Antonia da Silva", "m.antonia@email.com", birthDay, "m.tonia", "234", "988888");

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        Optional<List<UserDTO>> result = userService.findAllUsers();

        assertTrue(result.isPresent(), "Deve retornar um Optional com uma lista de UserDTO quando usuários são encontrados.");
        assertEquals(2, result.get().size(), "Deve conter dois UserDTO na lista.");
    }
}