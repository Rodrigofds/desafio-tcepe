package com.pitang.desafio.tcepe.repository;

import com.pitang.desafio.tcepe.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICarRepository extends JpaRepository<Car, Long> {
}
