package com.pitang.desafio.tcepe.repository;

import com.pitang.desafio.tcepe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<User, Long> {
}
