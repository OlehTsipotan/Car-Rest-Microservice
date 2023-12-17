package com.myapi.cars.service;

import com.myapi.cars.converter.MakeFromMakeDTOUpdater;
import com.myapi.cars.dto.MakeDTO;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class MakeServiceTest {

    private MakeService makeService;

    @Mock
    private MakeRepository makeRepository;

    @Mock
    private MakeEntityValidator makeEntityValidator;

    @Mock
    private ConverterService converterService;

    @Mock
    private MakeFromMakeDTOUpdater makeFromMakeDTOUpdater;

    @BeforeEach
    public void setUp() {
        makeService = new MakeService(makeRepository, makeEntityValidator, converterService, makeFromMakeDTOUpdater);
    }

    @ParameterizedTest
    @NullSource
    public void create_whenEntityIsNull_throwIllegalArgumentException(MakeDTO nullMakeDTO) {
        assertThrows(IllegalArgumentException.class, () -> makeService.create(nullMakeDTO));

        verifyNoInteractions(makeRepository);
        verifyNoInteractions(makeEntityValidator);
    }

    @Test
    public void create_whenRepositoryThrowsExceptionExtendsDataAccessException_throwServiceException() {
        doThrow(BadJpqlGrammarException.class).when(makeRepository).existsById(any());

        MakeDTO makeDTO = MakeDTO.builder().id(1L).build();
        Make make = Make.builder().id(1L).build();

        when(converterService.convert(makeDTO, Make.class)).thenReturn(make);

        assertThrows(ServiceException.class, () -> makeService.create(makeDTO));

        verify(makeEntityValidator).validate(any());
        verify(makeRepository).existsById(any());
        verifyNoMoreInteractions(makeRepository);
    }

    @Test
    public void create_whenValidatorThrowsValidationException_throwValidationException() {
        doThrow(ValidationException.class).when(makeEntityValidator).validate(any());

        assertThrows(ValidationException.class, () -> makeService.create(new MakeDTO()));

        verify(makeEntityValidator).validate(any());
    }

    @Test
    public void create_whenEntityAlreadyExists_throwEntityAlreadyExistsException() {
        when(makeRepository.existsById(any())).thenReturn(true);

        MakeDTO makeDTO = MakeDTO.builder().id(1L).build();
        Make make = Make.builder().id(1L).build();

        when(converterService.convert(makeDTO, Make.class)).thenReturn(make);

        assertThrows(EntityAlreadyExistsException.class, () -> makeService.create(makeDTO));

        verify(makeEntityValidator).validate(any());
    }

    @Test
    public void create_success() {
        MakeDTO makeDTO = MakeDTO.builder().id(1L).build();
        Make make = Make.builder().id(1L).build();

        when(converterService.convert(makeDTO, Make.class)).thenReturn(make);
        when(makeRepository.save(any())).thenReturn(make);
        when(makeRepository.existsById(any())).thenReturn(false);

        assertEquals(makeDTO.getId(), makeService.create(makeDTO));

        verify(makeRepository).save(make);
        verify(makeRepository).existsById(any());
        verifyNoMoreInteractions(makeRepository);

        verify(makeEntityValidator).validate(make);
        verifyNoMoreInteractions(makeEntityValidator);

        verify(converterService).convert(makeDTO, Make.class);
        verifyNoMoreInteractions(converterService);
    }

    @ParameterizedTest
    @NullSource
    public void update_whenEntityIsNull_throwIllegalArgumentException(MakeDTO nullMakeDTO) {
        assertThrows(IllegalArgumentException.class, () -> makeService.update(nullMakeDTO, 1L));

        verifyNoInteractions(makeRepository);
        verifyNoInteractions(makeEntityValidator);
    }

    @Test
    public void update_whenRepositoryThrowsExceptionExtendsDataAccessException_throwServiceException() {
        doThrow(BadJpqlGrammarException.class).when(makeRepository).findById(any());

        MakeDTO makeDTO = MakeDTO.builder().id(1L).build();

        assertThrows(ServiceException.class, () -> makeService.update(makeDTO, 1L));

        verify(makeRepository).findById(any());
        verifyNoMoreInteractions(makeRepository);
    }

    @Test
    public void update_whenValidatorThrowsValidationException_throwValidationException() {
        MakeDTO makeDTO = MakeDTO.builder().id(1L).build();
        Make make = Make.builder().id(1L).build();

        when(makeRepository.findById(any())).thenReturn(Optional.ofNullable(make));
        doThrow(ValidationException.class).when(makeEntityValidator).validate(any());

        assertThrows(ValidationException.class, () -> makeService.update(makeDTO, 1L));

        verify(makeEntityValidator).validate(any());

        verify(makeRepository).findById(any());
        verifyNoMoreInteractions(makeRepository);

        verify(makeFromMakeDTOUpdater).update(any(), any());
        verifyNoMoreInteractions(makeFromMakeDTOUpdater);

        verifyNoInteractions(converterService);
    }

    @Test
    public void update_whenEntityDoesNotExists_throwEntityNotFoundException() {
        when(makeRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> makeService.update(new MakeDTO(), 1L));

        verify(makeRepository).findById(any());
        verifyNoMoreInteractions(makeRepository);

        verifyNoInteractions(makeFromMakeDTOUpdater);
        verifyNoInteractions(converterService);
        verifyNoInteractions(makeEntityValidator);
    }

    @Test
    public void update_success() {
        MakeDTO makeDTO = MakeDTO.builder().id(1L).build();
        Make make = Make.builder().id(1L).build();

        when(makeRepository.findById(any())).thenReturn(Optional.ofNullable(make));
        when(makeRepository.save(any())).thenReturn(make);
        when(converterService.convert(make, MakeDTO.class)).thenReturn(makeDTO);

        assertEquals(makeDTO, makeService.update(makeDTO, 1L));

        verify(makeRepository).findById(any());
        verify(makeRepository).save(make);
        verifyNoMoreInteractions(makeRepository);

        verify(makeEntityValidator).validate(make);
        verifyNoMoreInteractions(makeEntityValidator);

        verify(makeFromMakeDTOUpdater).update(makeDTO, make);
        verifyNoMoreInteractions(makeFromMakeDTOUpdater);

        verify(converterService).convert(make, MakeDTO.class);
        verifyNoMoreInteractions(converterService);
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

        Pageable pageable = mock(Pageable.class);
        when(pageable.getSort()).thenReturn(mock(org.springframework.data.domain.Sort.class));

        assertDoesNotThrow(() -> makeService.findAll(pageable));

        verify(makeRepository).findAll(any(Pageable.class));
    }

    @ParameterizedTest
    @NullSource
    public void findById_whenIdIsNull_throwIllegalArgumentException(Long nullId) {
        assertThrows(IllegalArgumentException.class, () -> makeService.findById(nullId));
    }

    @Test
    public void findById_whenMakeRepositoryThrowsExceptionExtendsDataAccessException_throwServiceException() {
        doThrow(BadJpqlGrammarException.class).when(makeRepository).findById(any());

        assertThrows(ServiceException.class, () -> makeService.findById(1L));

        verify(makeRepository).findById(any());
    }

    @Test
    public void findById_success() {
        Make make = Make.builder().id(1L).build();
        MakeDTO makeDTO = MakeDTO.builder().id(1L).build();

        when(makeRepository.findById(any())).thenReturn(Optional.ofNullable(make));
        when(converterService.convert(make, MakeDTO.class)).thenReturn(makeDTO);

        assertEquals(makeDTO, makeService.findById(1L));

        verify(makeRepository).findById(any());
        verifyNoMoreInteractions(makeRepository);

        verify(converterService).convert(make, MakeDTO.class);
        verifyNoMoreInteractions(converterService);
    }
}
