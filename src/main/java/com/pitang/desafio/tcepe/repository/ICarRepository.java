package com.pitang.desafio.tcepe.repository;

import com.pitang.desafio.tcepe.model.Car;
import com.pitang.desafio.tcepe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICarRepository extends JpaRepository<Car, Long> {
    List<Car> findByUser(User user);
}
