package com.myapi.cars.service;

import com.myapi.cars.exception.EntityAlreadyExistsException;
import com.myapi.cars.exception.EntityNotFoundException;
import com.myapi.cars.exception.ServiceException;
import com.myapi.cars.exception.ValidationException;
import com.myapi.cars.model.Make;
import com.myapi.cars.repository.MakeRepository;
import com.myapi.cars.validation.MakeEntityValidator;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class MakeServiceTest {

    private MakeService makeService;

    @Mock
    private MakeRepository makeRepository;

    @Mock
    private MakeEntityValidator makeEntityValidator;

    @BeforeEach
    public void setUp() {
        makeService = new MakeService(makeRepository, makeEntityValidator);
    }

    @ParameterizedTest
    @NullSource
    public void create_whenEntityIsNull_throwIllegalArgumentException(Make nullMake) {
        assertThrows(IllegalArgumentException.class, () -> makeService.create(nullMake));

        verifyNoInteractions(makeRepository);
        verifyNoInteractions(makeEntityValidator);
    }

    @Test
    public void create_whenRepositoryThrowsExceptionExtendsDataAccessException_throwServiceException() {
        doThrow(BadJpqlGrammarException.class).when(makeRepository).save(any());

        assertThrows(ServiceException.class, () -> makeService.create(new Make()));

        verify(makeRepository).save(any());
    }

    @Test
    public void create_whenValidatorThrowsValidationException_throwValidationException() {
        doThrow(ValidationException.class).when(makeEntityValidator).validate(any());

        assertThrows(ValidationException.class, () -> makeService.create(new Make()));

        verify(makeEntityValidator).validate(any());
    }

    @Test
    public void create_whenEntityAlreadyExists_throwEntityAlreadyExistsException() {
        when(makeRepository.existsById(any())).thenReturn(true);

        Make make = new Make();
        make.setId(1L);

        assertThrows(EntityAlreadyExistsException.class, () -> makeService.create(make));

        verify(makeEntityValidator).validate(any());
    }

    @Test
    public void create_success() {
        Make make = new Make();
        assertEquals(make.getId(), makeService.create(make));

        verify(makeRepository).save(make);
        verify(makeEntityValidator).validate(make);
    }

    @ParameterizedTest
    @NullSource
    public void update_whenEntityIsNull_throwIllegalArgumentException(Make nullMake) {
        assertThrows(IllegalArgumentException.class, () -> makeService.update(nullMake));

        verifyNoInteractions(makeRepository);
        verifyNoInteractions(makeEntityValidator);
    }

    @Test
    public void update_whenRepositoryThrowsExceptionExtendsDataAccessException_throwServiceException() {
        when(makeRepository.existsById(any())).thenReturn(true);
        doThrow(BadJpqlGrammarException.class).when(makeRepository).save(any());

        Make make = new Make();
        make.setId(1L);

        assertThrows(ServiceException.class, () -> makeService.update(make));

        verify(makeRepository).save(any());
    }

    @Test
    public void update_whenValidatorThrowsValidationException_throwValidationException() {
        doThrow(ValidationException.class).when(makeEntityValidator).validate(any());

        Make make = new Make();
        make.setId(1L);

        assertThrows(ValidationException.class, () -> makeService.update(make));

        verify(makeEntityValidator).validate(any());
    }

    @Test
    public void update_whenEntityDoesNotExists_throwEntityAlreadyExistsException() {
        when(makeRepository.existsById(any())).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> makeService.update(new Make()));

        verify(makeEntityValidator).validate(any());
    }

    @Test
    public void update_success() {
        when(makeRepository.existsById(any())).thenReturn(true);

        Make make = new Make();
        make.setId(1L);
        assertEquals(make, makeService.update(make));

        verify(makeRepository).save(make);
        verify(makeEntityValidator).validate(make);
    }

    @Test
    public void deleteById_whenRepositoryThrowsExceptionExtendsDataAccessException_throwServiceException() {
        doThrow(BadJpqlGrammarException.class).when(makeRepository).deleteById(any());

        assertThrows(ServiceException.class, () -> makeService.deleteById(1L));

        verify(makeRepository).existsById(any());
    }

    @Test
    public void deleteById_whenEntityDoesNotExists_throwEntityDoesNotExistsException() {
        when(makeRepository.existsById(any())).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> makeService.deleteById(1L));

        verify(makeRepository).existsById(any());
    }

    @ParameterizedTest
    @NullSource
    public void deleteById_whenIdIsNull_throwIllegalArgumentException(Long nullId) {
        assertThrows(IllegalArgumentException.class, () -> makeService.deleteById(nullId));

        verifyNoInteractions(makeRepository);
    }

    @Test
    public void deleteById_success() {
        when(makeRepository.existsById(any())).thenReturn(true);

        makeService.deleteById(1L);

        verify(makeRepository).deleteById(any());
    }

    @ParameterizedTest
    @NullSource
    public void findAll_whenPageableIsNull_throwIllegalArgumentException(Pageable nullPageable) {
        assertThrows(IllegalArgumentException.class, () -> makeService.findAll(nullPageable));

        verifyNoInteractions(makeRepository);
    }

    @Test
    public void findAll_whenRepositoryThrowsExceptionExtendsDataAccessException_throwServiceException() {
        doThrow(BadJpqlGrammarException.class).when(makeRepository).findAll(any(Pageable.class));

        assertThrows(ServiceException.class, () -> makeService.findAll(Pageable.unpaged()));

        verify(makeRepository).findAll(any(Pageable.class));
    }

    @Test
    public void findAll_success() {
        when(makeRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of()));

        makeService.findAll(Pageable.unpaged());

        verify(makeRepository).findAll(any(Pageable.class));
    }
}
