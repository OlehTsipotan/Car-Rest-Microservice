package com.myapi.cars.service;

import com.myapi.cars.exception.EntityAlreadyExistsException;
import com.myapi.cars.exception.EntityNotFoundException;
import com.myapi.cars.exception.ServiceException;
import com.myapi.cars.model.Car;
import com.myapi.cars.repository.CarRepository;
import com.myapi.cars.validation.CarEntityValidator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CarService {

    private final CarRepository carRepository;

    private final CarEntityValidator carEntityValidator;

    @Transactional
    public Long create(@NonNull Car car) {
        execute(() -> {
            carEntityValidator.validate(car);
            if (car.getId() != null && carRepository.existsById(car.getId())) {
                throw new EntityAlreadyExistsException("Car with id = " + car.getId() + " already exists");
            }
            carRepository.save(car);
        });
        log.info("saved {}", car);
        return car.getId();
    }

    @Transactional
    public Car update(@NonNull Car car) {
        execute(() -> {
            carEntityValidator.validate(car);
            if (car.getId() == null || !carRepository.existsById(car.getId())) {
                throw new EntityNotFoundException("Car with id = " + car.getId() + " do not exists");
            }
            carRepository.save(car);
        });
        log.info("updated {}", car);
        return car;
    }

    @Transactional
    public void deleteById(@NonNull Long id) {
        execute(() -> {
            if (!carRepository.existsById(id)) {
                throw new EntityNotFoundException("There is no Car to delete with id = " + id);
            }
            carRepository.deleteById(id);
        });
        log.info("Deleted id = {}", id);
    }

    public List<Car> findAll(@NonNull Pageable pageable) {
        List<Car> cars = execute(() -> carRepository.findAll(pageable)).stream().toList();
        log.debug("Retrieved All {} Cars", cars.size());
        return cars;
    }

    public List<Car> findAll(String makeName, Integer year, String model, @NonNull List<String> categoryNameList,
                             @NonNull Pageable pageable) {
        List<Car> cars =
                execute(() -> carRepository.findAll(makeName, year, model, categoryNameList, categoryNameList.size(),
                        pageable)).stream().toList();
        log.debug("Retrieved All {} Cars", cars.size());
        return cars;
    }

    public Car findById(@NonNull Long id) {
        Car car = execute(() -> carRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("There is no Make with id = " + id)));
        log.debug("Retrieved Car by id = {}", id);
        return car;
    }

    private <T> T execute(DaoSupplier<T> supplier) {
        try {
            return supplier.get();
        } catch (DataAccessException e) {
            throw new ServiceException("DAO operation failed", e);
        }
    }

    private void execute(DaoProcessor processor) {
        try {
            processor.process();
        } catch (DataAccessException e) {
            throw new ServiceException("DAO operation failed", e);
        }
    }

    @FunctionalInterface
    public interface DaoSupplier<T> {
        T get();
    }

    @FunctionalInterface
    public interface DaoProcessor {
        void process();
    }
}
