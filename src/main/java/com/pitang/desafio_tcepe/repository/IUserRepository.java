package com.pitang.desafio_tcepe.repository;

import com.pitang.desafio_tcepe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<User, Long> {
}
