package com.pitang.desafio.tcepe.service;

import com.pitang.desafio.tcepe.dto.CarDTO;
import com.pitang.desafio.tcepe.model.User;

import java.util.List;

public interface ICarService {

    List<CarDTO> findCarsByUser(User user);

    CarDTO findCarByIdUser(User user, Long id);

    CarDTO createCarForUser(User user, CarDTO carDTO);

    CarDTO updateCarById(User user, Long id, CarDTO carDTO);
}
