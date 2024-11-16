package com.pitang.desafio.tcepe.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pitang.desafio.tcepe.dto.CarDTO;
import com.pitang.desafio.tcepe.model.Car;
import com.pitang.desafio.tcepe.model.User;
import com.pitang.desafio.tcepe.repository.ICarRepository;
import com.pitang.desafio.tcepe.service.ICarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.pitang.desafio.tcepe.dto.CarDTO.toDTO;

@Service
public class CarServiceImpl implements ICarService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CarServiceImpl.class);
    private final ICarRepository repository;

    @Autowired
    public CarServiceImpl(final ICarRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<CarDTO> findCarsByUser(User user) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            final List<Car> cars = repository.findByUser(user);

            return cars.isEmpty()
                    ? new ArrayList<>()
                    : cars
                    .stream()
                    .map(car -> objectMapper
                            .convertValue(car, CarDTO.class))
                    .collect(Collectors.toList());

        } catch (RuntimeException e) {
            LOGGER.error("Error during search user's cars.");
            throw new RuntimeException(e.getMessage());
        }
    }

    @Transactional
    @Override
    public CarDTO findCarByUser(User user, Long id) {
        LOGGER.info("Start searching for car with ID: {}", id);

        Optional<Car> car = user.getCars().stream()
                .filter(value -> value.getId().equals(id))
                .findFirst();

        if (car.isEmpty()) {
            LOGGER.error("Car with ID {} not found for the authenticated user.", id);
            return null;
        }

        LOGGER.info("Car found: {}", car);
        return toDTO(car.get());
    }
}
