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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.pitang.desafio.tcepe.dto.UserDTO.toDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
    void shouldReturnAListOfAllUsersWithSuccess() {
        when(userRepository.findAll()).thenReturn(getUsers());

        List<UserDTO> result = userService.findAllUsers();

        assertEquals(2, result.size(), "Deve conter dois UserDTO na lista.");
    }

    @Test
    void shouldReturnAnUserIfEmailAndLoginValids() {
        UserDTO userDTO = getUserDTO();

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByLogin(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn(anyString());

        UserDTO createdUser = userService.createUser(userDTO);

        assertNotNull(createdUser);
        assertEquals("paulo.joao@email.com", createdUser.getEmail());
    }

    @Test
    void shouldThrowEmailExceptionIfEmailAlreadyExists() {
        UserDTO userDTO = getUserDTO();

        User existingUser = new User();
        existingUser.setEmail("paulo.joao@email.com");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(existingUser));

        assertThrows(EmailException.class, () -> userService.createUser(userDTO));
    }

    @Test
    void shouldThrowLoginExceptionIfLoginAlreadyExists() {
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
    @Test
    void shouldReturnUserDTOWhenUserIsFound() {
        Long userId = 1L;
        User user = getUsers().get(0);
        UserDTO userDTO = toDTO(user);
        when(userRepository.findById(eq(userId))).thenReturn(Optional.of(user));

        UserDTO result = userService.findUserById(userId);

        assertNotNull(result);
        assertEquals(userDTO.getId(), result.getId());
    }

    @Test
    void shouldReturnNullWhenUserNotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        UserDTO result = userService.findUserById(userId);

        assertNull(result);
    }

    @Test
    void shouldThrowRuntimeExceptionWhenErrorOccursDuringSearch() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                userService.findUserById(userId)
        );

        assertEquals("Database error", exception.getMessage());
    }

    @Test
    void shouldReturnUpdatedUserDTOWhenUpdateIsSuccessful() {
        Long userId = 1L;
        UserDTO dtoIn = getUserDTO();
        User updatedUser = getUsers().get(0);

        when(userRepository.findById(userId)).thenReturn(Optional.of(updatedUser));
        when(userRepository.saveAndFlush(any(User.class))).thenReturn(updatedUser);

        UserDTO result = userService.updateUserById(userId, dtoIn);

        assertNotNull(result);
        assertEquals(dtoIn.getFirstName(), result.getFirstName());
        assertEquals(dtoIn.getEmail(), result.getEmail());
    }

    @Test
    void shouldThrowRuntimeExceptionWhenErrorOccursDuringUpdate() {
        Long userId = 1L;
        UserDTO dtoIn = getUserDTO();

        when(userRepository.findById(userId)).thenReturn(Optional.of(getUsers().get(0)));
        when(userRepository.saveAndFlush(any(User.class))).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                userService.updateUserById(userId, dtoIn)
        );
        assertEquals("Database error", exception.getMessage());
    }

    @Test
    void shouldNotUpdateLastLoginWhenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        userService.updateLastLoginByUser(getUserDTO());

        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundToUpdate() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            userService.updateUserById(1L, getUserDTO());
        });
    }

    private static UserDTO getUserDTO() {
        Date birthDay = new Date(19800120);
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

    private static List<User> getUsers() {
        List<Car> carros1 = new ArrayList<>();
        List<Car> carros2 = new ArrayList<>();
        Date birthDay = new Date(19800120L);
        Date createdAt = new Date(20241102L);
        Date lastLogin = new Date(20240120L);

        Date birthDay2 = new Date(19800118L);
        Date createdAt2 = new Date(20241103L);
        Date lastLogin2 = new Date(20240124L);

        return List
                .of(new User(1L, "João", "Paulo da Silva", "paulo.joao@email.com", birthDay,
                                "jp.silva", "123", "999999", carros1, createdAt, lastLogin),
                        new User(2L, "Maria", "Antonia da Silva", "m.antonia@email.com",
                                birthDay2, "m.tonia", "234", "988888", carros2, createdAt2, lastLogin2));
    }
}