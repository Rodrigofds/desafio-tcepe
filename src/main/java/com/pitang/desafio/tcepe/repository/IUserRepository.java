package com.pitang.desafio.tcepe.repository;

import com.pitang.desafio.tcepe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByLogin(String emailOrlogin);

}
