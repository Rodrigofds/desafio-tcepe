package com.pitang.desafio.tcepe.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pitang.desafio.tcepe.dto.CarDTO;
import com.pitang.desafio.tcepe.exception.expections.CarNotFoundException;
import com.pitang.desafio.tcepe.exception.expections.ErrorMessage;
import com.pitang.desafio.tcepe.exception.expections.InvalidsFieldsException;
import com.pitang.desafio.tcepe.exception.expections.LicensePlateExistsException;
import com.pitang.desafio.tcepe.model.Car;
import com.pitang.desafio.tcepe.model.User;
import com.pitang.desafio.tcepe.repository.ICarRepository;
import com.pitang.desafio.tcepe.repository.IUserRepository;
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

import static com.pitang.desafio.tcepe.dto.CarDTO.fromDTO;
import static com.pitang.desafio.tcepe.dto.CarDTO.toDTO;

@Service
public class CarServiceImpl implements ICarService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CarServiceImpl.class);
    public static final String BRAZILIAN_LICENSE_PLATE_MODEL = "^[A-Z]{3}-\\d{1}[A-Z0-9]{1}\\d{2}$";
    private final ICarRepository repository;
    private final IUserRepository userRepository;

    @Autowired
    public CarServiceImpl(final ICarRepository repository, IUserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
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
    public CarDTO findCarByIdUser(User user, Long id) {
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

    @Transactional
    public CarDTO createCarForUser(User user, CarDTO carDTO) {
        validateCarFields(carDTO);

        if (repository.existsByLicensePlate(carDTO.getLicensePlate())) {
            throw new LicensePlateExistsException(new ErrorMessage("License plate already exists", 1025));
        }

        Car newCar = fromDTO(carDTO);

        User userFound = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        newCar.setUser(userFound);

        Car savedCar = repository.save(newCar);

        return toDTO(savedCar);
    }

    private void validateCarFields(CarDTO carDTO) {
        if (!carDTO.getLicensePlate().matches(BRAZILIAN_LICENSE_PLATE_MODEL)) {
            throw new InvalidsFieldsException(new ErrorMessage("Invalid fields", 1026));
        }
    }

    @Transactional
    public CarDTO updateCarById(User user, Long carId, CarDTO carDTO) {
        validateCarFields(carDTO);

        Car carFound = repository.findCarByUserAndId(user, carId)
                .orElseThrow(() -> new CarNotFoundException(new ErrorMessage("Car not found", 1027)));

        if (!carFound.getLicensePlate().equals(carDTO.getLicensePlate()) &&
                repository.existsByLicensePlate(carDTO.getLicensePlate())) {
            throw new LicensePlateExistsException(new ErrorMessage("License plate already exists", 1025));
        }

        carFound.setLicensePlate(carDTO.getLicensePlate());
        carFound.setModel(carDTO.getModel());
        carFound.setYear(carDTO.getYear());
        carFound.setColor(carDTO.getColor());

        Car updatedCar = repository.save(carFound);

        return toDTO(updatedCar);
    }
}
