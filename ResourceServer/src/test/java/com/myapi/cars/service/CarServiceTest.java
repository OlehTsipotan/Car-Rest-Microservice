package com.myapi.cars.service;

import com.myapi.cars.converter.CarFromCarDTOUpdater;
import com.myapi.cars.converter.CarFromCarDTOUpdaterTest;
import com.myapi.cars.dto.CarDTO;
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
import java.util.Optional;

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

    @Mock
    private ConverterService converterService;

    @Mock
    private CarFromCarDTOUpdater carFromCarDTOUpdater;

    @BeforeEach
    public void setUp() {
        carService = new CarService(carRepository, carEntityValidator, converterService, carFromCarDTOUpdater);
    }

    @ParameterizedTest
    @NullSource
    public void create_whenEntityIsNull_throwIllegalArgumentException(CarDTO nullCarDTO) {
        assertThrows(IllegalArgumentException.class, () -> carService.create(nullCarDTO));

        verifyNoInteractions(carRepository);
        verifyNoInteractions(carEntityValidator);
    }

    @Test
    public void create_whenRepositoryThrowsExceptionExtendsDataAccessException_throwServiceException() {
        doThrow(BadJpqlGrammarException.class).when(carRepository).existsById(any());

        CarDTO carDTO = CarDTO.builder().id(1L).build();
        Car car = Car.builder().id(1L).build();

        when(converterService.convert(carDTO, Car.class)).thenReturn(car);

        assertThrows(ServiceException.class, () -> carService.create(carDTO));

        verify(carEntityValidator).validate(any());
        verify(carRepository).existsById(any());
        verifyNoMoreInteractions(carRepository);
    }

    @Test
    public void create_whenValidatorThrowsValidationException_throwValidationException() {
        doThrow(ValidationException.class).when(carEntityValidator).validate(any());

        assertThrows(ValidationException.class, () -> carService.create(new CarDTO()));

        verify(carEntityValidator).validate(any());
    }

    @Test
    public void create_whenEntityAlreadyExists_throwEntityAlreadyExistsException() {
        when(carRepository.existsById(any())).thenReturn(true);

        CarDTO carDTO = CarDTO.builder().id(1L).build();
        Car car = Car.builder().id(1L).build();

        when(converterService.convert(carDTO, Car.class)).thenReturn(car);

        assertThrows(EntityAlreadyExistsException.class, () -> carService.create(carDTO));

        verify(carEntityValidator).validate(any());
    }

    @Test
    public void create_success() {
        CarDTO carDTO = CarDTO.builder().id(1L).build();
        Car car = Car.builder().id(1L).build();

        when(converterService.convert(carDTO, Car.class)).thenReturn(car);
        when(carRepository.save(any())).thenReturn(car);
        when(carRepository.existsById(any())).thenReturn(false);

        assertEquals(carDTO.getId(), carService.create(carDTO));

        verify(carRepository).save(car);
        verify(carRepository).existsById(any());
        verifyNoMoreInteractions(carRepository);

        verify(carEntityValidator).validate(car);
        verifyNoMoreInteractions(carEntityValidator);

        verify(converterService).convert(carDTO, Car.class);
        verifyNoMoreInteractions(converterService);
    }

    @ParameterizedTest
    @NullSource
    public void update_whenEntityIsNull_throwIllegalArgumentException(CarDTO nullCarDTO) {
        assertThrows(IllegalArgumentException.class, () -> carService.update(nullCarDTO, 1L));

        verifyNoInteractions(carRepository);
        verifyNoInteractions(carEntityValidator);
    }

    @Test
    public void update_whenRepositoryThrowsExceptionExtendsDataAccessException_throwServiceException() {
        doThrow(BadJpqlGrammarException.class).when(carRepository).findById(any());

        CarDTO carDTO = CarDTO.builder().id(1L).build();

        assertThrows(ServiceException.class, () -> carService.update(carDTO, 1L));

        verify(carRepository).findById(any());
        verifyNoMoreInteractions(carRepository);
    }

    @Test
    public void update_whenValidatorThrowsValidationException_throwValidationException() {
        CarDTO carDTO = CarDTO.builder().id(1L).build();
        Car car = Car.builder().id(1L).build();

        when(carRepository.findById(any())).thenReturn(Optional.ofNullable(car));
        doThrow(ValidationException.class).when(carEntityValidator).validate(any());

        assertThrows(ValidationException.class, () -> carService.update(carDTO, 1L));

        verify(carEntityValidator).validate(any());

        verify(carRepository).findById(any());
        verifyNoMoreInteractions(carRepository);

        verify(carFromCarDTOUpdater).update(any(), any());
        verifyNoMoreInteractions(carFromCarDTOUpdater);

        verifyNoInteractions(converterService);
    }

    @Test
    public void update_whenEntityDoesNotExists_throwEntityNotFoundException() {
        when(carRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> carService.update(new CarDTO(), 1L));

        verify(carRepository).findById(any());
        verifyNoMoreInteractions(carRepository);

        verifyNoInteractions(carFromCarDTOUpdater);
        verifyNoInteractions(converterService);
        verifyNoInteractions(carEntityValidator);
    }

    @Test
    public void update_success() {
        CarDTO carDTO = CarDTO.builder().id(1L).build();
        Car car = Car.builder().id(1L).build();

        when(carRepository.findById(any())).thenReturn(Optional.ofNullable(car));
        when(carRepository.save(any())).thenReturn(car);
        when(converterService.convert(car, CarDTO.class)).thenReturn(carDTO);

        assertEquals(carDTO, carService.update(carDTO, 1L));

        verify(carRepository).findById(any());
        verify(carRepository).save(car);
        verifyNoMoreInteractions(carRepository);

        verify(carEntityValidator).validate(car);
        verifyNoMoreInteractions(carEntityValidator);

        verify(carFromCarDTOUpdater).update(carDTO, car);
        verifyNoMoreInteractions(carFromCarDTOUpdater);

        verify(converterService).convert(car, CarDTO.class);
        verifyNoMoreInteractions(converterService);
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

    @ParameterizedTest
    @NullSource
    public void findById_whenIdIsNull_throwIllegalArgumentException(Long nullId) {
        assertThrows(IllegalArgumentException.class, () -> carService.findById(nullId));
    }

    @Test
    public void findById_whenCarRepositoryThrowsExceptionExtendsDataAccessException_throwServiceException() {
        doThrow(BadJpqlGrammarException.class).when(carRepository).findById(any());

        assertThrows(ServiceException.class, () -> carService.findById(1L));

        verify(carRepository).findById(any());
    }

    @Test
    public void findById_success() {
        Car car = Car.builder().id(1L).build();
        CarDTO carDTO = CarDTO.builder().id(1L).build();

        when(carRepository.findById(any())).thenReturn(Optional.ofNullable(car));
        when(converterService.convert(car, CarDTO.class)).thenReturn(carDTO);

        assertEquals(carDTO, carService.findById(1L));

        verify(carRepository).findById(any());
        verifyNoMoreInteractions(carRepository);

        verify(converterService).convert(car, CarDTO.class);
        verifyNoMoreInteractions(converterService);
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
    public void findAll_success() {
        when(carRepository.findAll(any(), any(), any(), any(), any(), any(Pageable.class))).thenReturn(new PageImpl<>(List.of()));

        Pageable pageable = mock(Pageable.class);
        when(pageable.getSort()).thenReturn(mock(org.springframework.data.domain.Sort.class));

        assertDoesNotThrow(() -> carService.findAll("make", 1000, "model", List.of(), pageable));

        verify(carRepository).findAll(any(), any(), any(), any(), any(), any(Pageable.class));
    }
}

