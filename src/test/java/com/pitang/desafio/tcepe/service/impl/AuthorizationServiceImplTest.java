package com.pitang.desafio.tcepe.service.impl;

import com.pitang.desafio.tcepe.model.User;
import com.pitang.desafio.tcepe.repository.IUserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuthorizationServiceImplTest {

    private final IUserRepository repository = Mockito.mock(IUserRepository.class);
    private final AuthorizationServiceImpl service = new AuthorizationServiceImpl(repository);

    @Test
    void shouldReturnUserWhenLoginExists() {
        User mockUser = new User();
        mockUser.setLogin("user.login");
        Mockito.when(repository.findByLogin("user.login")).thenReturn(Optional.of(mockUser));

        UserDetails userDetails = service.loadUserByUsername("user.login");

        assertNotNull(userDetails);
        assertEquals("user.login", userDetails.getUsername());
    }

    @Test
    void shouldThrowExceptionWhenLoginDoesNotExist() {
        Mockito.when(repository.findByLogin("invalidUser")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
                service.loadUserByUsername("invalidUser")
        );
    }
}
