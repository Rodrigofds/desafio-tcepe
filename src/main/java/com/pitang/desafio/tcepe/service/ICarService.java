package com.pitang.desafio.tcepe.service;

import com.pitang.desafio.tcepe.dto.CarDTO;
import com.pitang.desafio.tcepe.model.User;

import java.util.List;

public interface ICarService {

    List<CarDTO> findCarsByUser(User user);

    CarDTO findCarByUser(User user, Long id);
}
