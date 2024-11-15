package com.pitang.desafio.tcepe.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pitang.desafio.tcepe.dto.CarDTO;
import com.pitang.desafio.tcepe.model.Car;
import com.pitang.desafio.tcepe.repository.ICarRepository;
import com.pitang.desafio.tcepe.service.ICarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarServiceImpl implements ICarService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CarServiceImpl.class);
    private final ICarRepository repository;

    @Autowired
    public CarServiceImpl(final ICarRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<CarDTO> findAllCars() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            final List<Car> cars = repository.findAll();

            return cars.isEmpty()
                    ? new ArrayList<>()
                    : cars
                    .stream()
                    .map(car -> objectMapper
                            .convertValue(car, CarDTO.class))
                    .collect(Collectors.toList());

        } catch (RuntimeException e) {
            LOGGER.error("Error during all users search.");
            throw new RuntimeException(e.getMessage());
        }
    }
}
