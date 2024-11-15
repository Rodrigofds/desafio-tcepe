package com.pitang.desafio.tcepe.service.impl;

import com.pitang.desafio.tcepe.model.User;
import com.pitang.desafio.tcepe.repository.IUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationServiceImpl implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationServiceImpl.class);
    private final IUserRepository repository;

    @Autowired
    public AuthorizationServiceImpl(final IUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOGGER.info("Start login search: {}", username);

        User user = repository.findByLogin(username)
                .orElseThrow(() -> {
                    LOGGER.error("Invalid login or password: {}", username);
                    return new UsernameNotFoundException("Invalid login or password: " + username);
                });

        LOGGER.info("User found: {}", user.getUsername());
        return user;
    }
}
