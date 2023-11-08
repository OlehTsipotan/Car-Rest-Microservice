package com.myapi.cars.service;

import com.myapi.cars.converter.CarFromCarDTOUpdater;
import com.myapi.cars.dto.CarDTO;
import com.myapi.cars.dto.DTOSearchResponse;
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

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CarService {

    private final CarRepository carRepository;

    private final CarEntityValidator carEntityValidator;

    private final ConverterService converterService;

    private final CarFromCarDTOUpdater carFromCarDTOUpdater;

    @Transactional
    public Long create(@NonNull CarDTO carDTO) {
        Car car = convertToEntity(carDTO);
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
    public CarDTO update(@NonNull CarDTO carDTO, @NonNull Long id) {
        Car carToUpdate = execute(() -> {
            Car car = carRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("There is no Car to update with id = " + id));

            carFromCarDTOUpdater.update(carDTO, car);
            carEntityValidator.validate(car);

            return carRepository.save(car);
        });
        log.info("updated {}", carToUpdate);
        return convertToDTO(carToUpdate);
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

    public DTOSearchResponse findAllAsDTO(@NonNull Pageable pageable) {
        List<CarDTO> carDTOList =
                execute(() -> carRepository.findAll(pageable)).stream().map(this::convertToDTO).toList();
        log.debug("Retrieved All {} Cars", carDTOList.size());
        return DTOSearchResponse.builder().offset(pageable.getOffset()).limit(pageable.getPageSize())
                .total(carDTOList.size()).sort(pageable.getSort().toString()).data(carDTOList).build();
    }

    public DTOSearchResponse findAllAsDTO(String makeName, Integer year, String model,
                                          @NonNull List<String> carNameList, @NonNull Pageable pageable) {
        List<CarDTO> carDTOList =
                execute(() -> carRepository.findAll(makeName, year, model, carNameList, carNameList.size(),
                        pageable)).stream().map(this::convertToDTO).toList();
        log.debug("Retrieved All {} Cars", carDTOList.size());
        return DTOSearchResponse.builder().offset(pageable.getOffset()).limit(pageable.getPageSize())
                .total(carDTOList.size()).sort(pageable.getSort().toString()).data(carDTOList).build();

    }

    public CarDTO findByIdAsDTO(@NonNull Long id) {
        Car car = execute(() -> carRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("There is no Car with id = " + id)));
        log.debug("Retrieved Car by id = {}", id);
        return convertToDTO(car);
    }

    private CarDTO convertToDTO(Car car) {
        return converterService.convert(car, CarDTO.class);
    }

    private Car convertToEntity(CarDTO carDTO) {
        return converterService.convert(carDTO, Car.class);
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
