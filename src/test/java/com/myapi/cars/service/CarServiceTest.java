package com.myapi.cars.service;

import com.myapi.cars.exception.EntityAlreadyExistsException;
import com.myapi.cars.exception.EntityNotFoundException;
import com.myapi.cars.exception.ServiceException;
import com.myapi.cars.exception.ValidationException;
import com.myapi.cars.model.Car;
import com.myapi.cars.repository.CarRepository;
import com.myapi.cars.validation.CarEntityValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.BadJpqlGrammarException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class CarServiceTest {

    private CarService carService;

    @Mock
    private CarRepository carRepository;

    @Mock
    private CarEntityValidator carEntityValidator;

    @BeforeEach
    public void setUp() {
        carService = new CarService(carRepository, carEntityValidator);
    }

    @ParameterizedTest
    @NullSource
    public void create_whenEntityIsNull_throwIllegalArgumentException(Car nullCar) {
        assertThrows(IllegalArgumentException.class, () -> carService.create(nullCar));

        verifyNoInteractions(carRepository);
        verifyNoInteractions(carEntityValidator);
    }

    @Test
    public void create_whenRepositoryThrowsExceptionExtendsDataAccessException_throwServiceException() {
        doThrow(BadJpqlGrammarException.class).when(carRepository).save(any());

        assertThrows(ServiceException.class, () -> carService.create(new Car()));

        verify(carRepository).save(any());
    }

    @Test
    public void create_whenValidatorThrowsValidationException_throwValidationException() {
        doThrow(ValidationException.class).when(carEntityValidator).validate(any());

        assertThrows(ValidationException.class, () -> carService.create(new Car()));

        verify(carEntityValidator).validate(any());
    }

    @Test
    public void create_whenEntityAlreadyExists_throwEntityAlreadyExistsException() {
        when(carRepository.existsById(any())).thenReturn(true);

        Car car = Car.builder().id(1L).build();

        assertThrows(EntityAlreadyExistsException.class, () -> carService.create(car));

        verify(carEntityValidator).validate(any());
    }

    @Test
    public void create_success() {
        Car car = new Car();
        assertEquals(car.getId(), carService.create(car));

        verify(carRepository).save(car);
        verify(carEntityValidator).validate(car);
    }

    @ParameterizedTest
    @NullSource
    public void update_whenEntityIsNull_throwIllegalArgumentException(Car nullCar) {
        assertThrows(IllegalArgumentException.class, () -> carService.update(nullCar));

        verifyNoInteractions(carRepository);
        verifyNoInteractions(carEntityValidator);
    }

    @Test
    public void update_whenRepositoryThrowsExceptionExtendsDataAccessException_throwServiceException() {
        when(carRepository.existsById(any())).thenReturn(true);
        doThrow(BadJpqlGrammarException.class).when(carRepository).save(any());

        Car car = Car.builder().id(1L).build();

        assertThrows(ServiceException.class, () -> carService.update(car));

        verify(carRepository).save(any());
    }

    @Test
    public void update_whenValidatorThrowsValidationException_throwValidationException() {
        doThrow(ValidationException.class).when(carEntityValidator).validate(any());

        assertThrows(ValidationException.class, () -> carService.update(new Car()));

        verify(carEntityValidator).validate(any());
    }

    @Test
    public void update_whenEntityDoesNotExists_throwEntityAlreadyExistsException() {
        when(carRepository.existsById(any())).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> carService.update(new Car()));

        verify(carEntityValidator).validate(any());
    }

    @Test
    public void update_success() {
        when(carRepository.existsById(any())).thenReturn(true);

        Car car = Car.builder().id(1L).build();
        assertEquals(car, carService.update(car));

        verify(carRepository).save(car);
        verify(carEntityValidator).validate(car);
    }

    @Test
    public void deleteById_whenRepositoryThrowsExceptionExtendsDataAccessException_throwServiceException() {
        doThrow(BadJpqlGrammarException.class).when(carRepository).deleteById(any());

        assertThrows(ServiceException.class, () -> carService.deleteById(1L));

        verify(carRepository).existsById(any());
    }

    @Test
    public void deleteById_whenEntityDoesNotExists_throwEntityDoesNotExistsException() {
        when(carRepository.existsById(any())).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> carService.deleteById(1L));

        verify(carRepository).existsById(any());
    }

    @ParameterizedTest
    @NullSource
    public void deleteById_whenIdIsNull_throwIllegalArgumentException(Long nullId) {
        assertThrows(IllegalArgumentException.class, () -> carService.deleteById(nullId));

        verifyNoInteractions(carRepository);
    }

    @Test
    public void deleteById_success() {
        when(carRepository.existsById(any())).thenReturn(true);

        carService.deleteById(1L);

        verify(carRepository).deleteById(any());
    }

    @Test
    public void findAll_withOnlyPageable_whenRepositoryThrowsExceptionExtendsDataAccessException_throwServiceException() {
        doThrow(BadJpqlGrammarException.class).when(carRepository).findAll(any(Pageable.class));

        assertThrows(ServiceException.class, () -> carService.findAll(Pageable.unpaged()));

        verify(carRepository).findAll(any(Pageable.class));
    }

    @ParameterizedTest
    @NullSource
    public void findAll_withOnlyPageable_whenPageableIsNull_throwIllegalArgumentException(Pageable nullPageable) {
        assertThrows(IllegalArgumentException.class, () -> carService.findAll(nullPageable));

        verifyNoInteractions(carRepository);
    }

    @Test
    public void findAll_whenRepositoryThrowsExceptionExtendsDataAccessException_throwServiceException() {
        when(carRepository.findAll(any(), any(), any(), any(), any(), any(Pageable.class))).thenThrow(
                BadJpqlGrammarException.class);

        assertThrows(ServiceException.class,
                () -> carService.findAll("make", 1000, "model", List.of(), Pageable.unpaged()));

        verify(carRepository).findAll(any(), any(), any(), any(), any(), any(Pageable.class));
    }

    @ParameterizedTest
    @NullSource
    public void findAll_whenListInNull_throwIllegalArgumentException(List<String> nullList) {
        assertThrows(IllegalArgumentException.class,
                () -> carService.findAll("make", 1000, "model", nullList, Pageable.unpaged()));

        verifyNoInteractions(carRepository);
    }

    @ParameterizedTest
    @NullSource
    public void findAll_whenPageableIsNull_throwIllegalArgumentException(Pageable nullPageable) {
        assertThrows(IllegalArgumentException.class,
                () -> carService.findAll("make", 1000, "model", List.of(), nullPageable));

        verifyNoInteractions(carRepository);
    }

    @Test
    public void findAll_withOnlyPageable_success() {
        when(carRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of()));

        assertDoesNotThrow(() -> carService.findAll(Pageable.unpaged()));

        verify(carRepository).findAll(any(Pageable.class));
    }

    @Test
    public void findAll_success() {
        when(carRepository.findAll(any(), any(), any(), any(), any(), any(Pageable.class))).thenReturn(new PageImpl<>(List.of()));

        assertDoesNotThrow(() -> carService.findAll("make", 1000, "model", List.of(), Pageable.unpaged()));

        verify(carRepository).findAll(any(), any(), any(), any(), any(), any(Pageable.class));
    }
}

