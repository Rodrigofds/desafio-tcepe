package com.pitang.desafio.tcepe.controller;

import com.pitang.desafio.tcepe.dto.UserDTO;
import com.pitang.desafio.tcepe.exception.expections.ErrorMessage;
import com.pitang.desafio.tcepe.service.IUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private IUserService userService;

    @Test
    void shouldReturnUsersListsWithSuccess() {
        // Arrange
        List<UserDTO> mockUsers = List.of(
                new UserDTO(),
                new UserDTO()
        );
        Mockito.when(userService.findAllUsers()).thenReturn(mockUsers);

        // Act
        ResponseEntity<List<UserDTO>> response = userController.getAllUsers();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void shouldReturnNoContentWhenNoUsersFound() {
        // Arrange
        Mockito.when(userService.findAllUsers()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<UserDTO>> response = userController.getAllUsers();

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void shouldReturnInternalServerErrorWhenExceptionOccurs() {
        // Arrange
        Mockito.when(userService.findAllUsers()).thenThrow(new RuntimeException("Unexpected error"));

        // Act
        ResponseEntity<List<UserDTO>> response = userController.getAllUsers();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getUserById_ShouldReturn200WhenUserFound() {
        // Arrange
        Long userId = 1L;
        UserDTO mockUser = new UserDTO();
        mockUser.setId(userId);
        Mockito.when(userService.findUserById(userId)).thenReturn(mockUser);

        // Act
        ResponseEntity<Object> response = userController.getUserById(userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof UserDTO);
        assertEquals(mockUser, response.getBody());
    }

    @Test
    void shouldReturnNotFoundWhenUserIdNotFound() {
        // Arrange
        Long userId = 2L;
        Mockito.when(userService.findUserById(userId)).thenReturn(null);

        // Act
        ResponseEntity<Object> response = userController.getUserById(userId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void shouldReturnIllegalArgumentExceptionOccurs() {
        // Arrange
        Long invalidUserId = -1L;
        Mockito.when(userService.findUserById(invalidUserId))
                .thenThrow(new IllegalArgumentException("Invalid argument"));

        // Act
        ResponseEntity<Object> response = userController.getUserById(invalidUserId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ErrorMessage);

        ErrorMessage errorMessage = (ErrorMessage) response.getBody();
        assertEquals("Invalid argument", errorMessage.getError());
        assertEquals(1846, errorMessage.getErrorCode());
    }

    @Test
    void shouldReturnInternalServerErrorMessageWhenUnexpectedErrorOccurs() {
        // Arrange
        Long userId = 4L;
        Mockito.when(userService.findUserById(userId))
                .thenThrow(new RuntimeException("Unexpected error"));

        // Act
        ResponseEntity<Object> response = userController.getUserById(userId);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ErrorMessage);

        ErrorMessage errorMessage = (ErrorMessage) response.getBody();
        assertEquals("Unexpected error", errorMessage.getError());
        assertEquals(103, errorMessage.getErrorCode());
    }
}
