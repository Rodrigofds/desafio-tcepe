package com.pitang.desafio.tcepe.repository;

import com.pitang.desafio.tcepe.model.Car;
import com.pitang.desafio.tcepe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ICarRepository extends JpaRepository<Car, Long> {
    List<Car> findByUser(User user);

    Car findCarByUserAndId(User user, Long id);

    boolean existsByLicensePlate(String licensePlate);

    Optional<Object> findByLicensePlate(String licensePlate);

    Car findCarByUserAndLicensePlate(User user, String licensePlate);
}
