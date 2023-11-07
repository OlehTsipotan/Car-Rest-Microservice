package com.myapi.cars.service;

import com.myapi.cars.exception.EntityAlreadyExistsException;
import com.myapi.cars.exception.EntityDoesNotExistsException;
import com.myapi.cars.exception.ServiceException;
import com.myapi.cars.exception.ValidationException;
import com.myapi.cars.model.Category;
import com.myapi.cars.repository.CategoryRepository;
import com.myapi.cars.validation.CategoryEntityValidator;
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

@ExtendWith(SpringExtension.class)
public class CategoryServiceTest {

    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryEntityValidator categoryEntityValidator;

    @BeforeEach
    public void setUp() {
        categoryService = new CategoryService(categoryRepository, categoryEntityValidator);
    }

    @ParameterizedTest
    @NullSource
    public void create_whenEntityIsNull_throwIllegalArgumentException(Category nullCategory) {
        assertThrows(IllegalArgumentException.class, () -> categoryService.create(nullCategory));

        verifyNoInteractions(categoryRepository);
        verifyNoInteractions(categoryEntityValidator);
    }

    @Test
    public void create_whenRepositoryThrowsExceptionExtendsDataAccessException_throwServiceException() {
        doThrow(BadJpqlGrammarException.class).when(categoryRepository).save(any());

        assertThrows(ServiceException.class, () -> categoryService.create(new Category()));

        verify(categoryRepository).save(any());
    }

    @Test
    public void create_whenValidatorThrowsValidationException_throwValidationException() {
        doThrow(ValidationException.class).when(categoryEntityValidator).validate(any());

        assertThrows(ValidationException.class, () -> categoryService.create(new Category()));

        verify(categoryEntityValidator).validate(any());
    }

    @Test
    public void create_whenEntityAlreadyExists_throwEntityAlreadyExistsException() {
        when(categoryRepository.existsById(any())).thenReturn(true);

        Category category = new Category();
        category.setId(1L);

        assertThrows(EntityAlreadyExistsException.class, () -> categoryService.create(category));

        verify(categoryEntityValidator).validate(any());
    }

    @Test
    public void create_success() {
        Category category = new Category();
        assertEquals(category.getId(), categoryService.create(category));

        verify(categoryRepository).save(category);
        verify(categoryEntityValidator).validate(category);
    }

    @ParameterizedTest
    @NullSource
    public void update_whenEntityIsNull_throwIllegalArgumentException(Category nullCategory) {
        assertThrows(IllegalArgumentException.class, () -> categoryService.update(nullCategory));

        verifyNoInteractions(categoryRepository);
        verifyNoInteractions(categoryEntityValidator);
    }

    @Test
    public void update_whenRepositoryThrowsExceptionExtendsDataAccessException_throwServiceException() {
        when(categoryRepository.existsById(any())).thenReturn(true);
        doThrow(BadJpqlGrammarException.class).when(categoryRepository).save(any());

        Category category = new Category();
        category.setId(1L);

        assertThrows(ServiceException.class, () -> categoryService.update(category));

        verify(categoryRepository).save(any());
    }

    @Test
    public void update_whenValidatorThrowsValidationException_throwValidationException() {
        doThrow(ValidationException.class).when(categoryEntityValidator).validate(any());

        assertThrows(ValidationException.class, () -> categoryService.update(new Category()));

        verify(categoryEntityValidator).validate(any());
    }

    @Test
    public void update_whenEntityDoesNotExists_throwEntityAlreadyExistsException() {
        when(categoryRepository.existsById(any())).thenReturn(false);

        assertThrows(EntityDoesNotExistsException.class, () -> categoryService.update(new Category()));

        verify(categoryEntityValidator).validate(any());
    }

    @Test
    public void update_success() {
        when(categoryRepository.existsById(any())).thenReturn(true);

        Category category = new Category();
        category.setId(1L);
        assertEquals(category, categoryService.update(category));

        verify(categoryRepository).save(category);
        verify(categoryEntityValidator).validate(category);
    }

    @Test
    public void deleteById_whenRepositoryThrowsExceptionExtendsDataAccessException_throwServiceException() {
        doThrow(BadJpqlGrammarException.class).when(categoryRepository).deleteById(any());

        assertThrows(ServiceException.class, () -> categoryService.deleteById(1L));

        verify(categoryRepository).existsById(any());
    }

    @Test
    public void deleteById_whenEntityDoesNotExists_throwEntityDoesNotExistsException() {
        when(categoryRepository.existsById(any())).thenReturn(false);

        assertThrows(EntityDoesNotExistsException.class, () -> categoryService.deleteById(1L));

        verify(categoryRepository).existsById(any());
    }

    @ParameterizedTest
    @NullSource
    public void deleteById_whenIdIsNull_throwIllegalArgumentException(Long nullId) {
        assertThrows(IllegalArgumentException.class, () -> categoryService.deleteById(nullId));

        verifyNoInteractions(categoryRepository);
    }

    @Test
    public void deleteById_success() {
        when(categoryRepository.existsById(any())).thenReturn(true);

        categoryService.deleteById(1L);

        verify(categoryRepository).deleteById(any());
    }

    @ParameterizedTest
    @NullSource
    public void findAll_whenPageableIsNull_throwIllegalArgumentException(Pageable nullPageable) {
        assertThrows(IllegalArgumentException.class, () -> categoryService.findAll(nullPageable));

        verifyNoInteractions(categoryRepository);
    }

    @Test
    public void findAll_whenRepositoryThrowsExceptionExtendsDataAccessException_throwServiceException() {
        doThrow(BadJpqlGrammarException.class).when(categoryRepository).findAll(any(Pageable.class));

        assertThrows(ServiceException.class, () -> categoryService.findAll(Pageable.unpaged()));

        verify(categoryRepository).findAll(any(Pageable.class));
    }

    @Test
    public void findAll_success() {
        when(categoryRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of()));

        categoryService.findAll(Pageable.unpaged());

        verify(categoryRepository).findAll(any(Pageable.class));
    }
}
