package com.myapi.cars.service;

import com.myapi.cars.converter.CategoryFromCategoryDTOUpdater;
import com.myapi.cars.dto.CategoryDTO;
import com.myapi.cars.exception.EntityAlreadyExistsException;
import com.myapi.cars.exception.EntityNotFoundException;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class CategoryServiceTest {

    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryEntityValidator categoryEntityValidator;

    @Mock
    private ConverterService converterService;

    @Mock
    private CategoryFromCategoryDTOUpdater categoryFromCategoryDTOUpdater;

    @BeforeEach
    public void setUp() {
        categoryService = new CategoryService(categoryRepository, categoryEntityValidator, converterService,
                categoryFromCategoryDTOUpdater);
    }

    @ParameterizedTest
    @NullSource
    public void create_whenEntityIsNull_throwIllegalArgumentException(CategoryDTO nullCategoryDTO) {
        assertThrows(IllegalArgumentException.class, () -> categoryService.create(nullCategoryDTO));

        verifyNoInteractions(categoryRepository);
        verifyNoInteractions(categoryEntityValidator);
    }

    @Test
    public void create_whenRepositoryThrowsExceptionExtendsDataAccessException_throwServiceException() {
        doThrow(BadJpqlGrammarException.class).when(categoryRepository).existsById(any());

        CategoryDTO categoryDTO = CategoryDTO.builder().id(1L).build();
        Category category = Category.builder().id(1L).build();

        when(converterService.convert(categoryDTO, Category.class)).thenReturn(category);

        assertThrows(ServiceException.class, () -> categoryService.create(categoryDTO));

        verify(categoryEntityValidator).validate(any());
        verify(categoryRepository).existsById(any());
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    public void create_whenValidatorThrowsValidationException_throwValidationException() {
        doThrow(ValidationException.class).when(categoryEntityValidator).validate(any());

        assertThrows(ValidationException.class, () -> categoryService.create(new CategoryDTO()));

        verify(categoryEntityValidator).validate(any());
    }

    @Test
    public void create_whenEntityAlreadyExists_throwEntityAlreadyExistsException() {
        when(categoryRepository.existsById(any())).thenReturn(true);

        CategoryDTO categoryDTO = CategoryDTO.builder().id(1L).build();
        Category category = Category.builder().id(1L).build();

        when(converterService.convert(categoryDTO, Category.class)).thenReturn(category);

        assertThrows(EntityAlreadyExistsException.class, () -> categoryService.create(categoryDTO));

        verify(categoryEntityValidator).validate(any());
    }

    @Test
    public void create_success() {
        CategoryDTO categoryDTO = CategoryDTO.builder().id(1L).build();
        Category category = Category.builder().id(1L).build();

        when(converterService.convert(categoryDTO, Category.class)).thenReturn(category);
        when(categoryRepository.save(any())).thenReturn(category);
        when(categoryRepository.existsById(any())).thenReturn(false);

        assertEquals(categoryDTO.getId(), categoryService.create(categoryDTO));

        verify(categoryRepository).save(category);
        verify(categoryRepository).existsById(any());
        verifyNoMoreInteractions(categoryRepository);

        verify(categoryEntityValidator).validate(category);
        verifyNoMoreInteractions(categoryEntityValidator);

        verify(converterService).convert(categoryDTO, Category.class);
        verifyNoMoreInteractions(converterService);
    }

    @ParameterizedTest
    @NullSource
    public void update_whenEntityIsNull_throwIllegalArgumentException(CategoryDTO nullCategoryDTO) {
        assertThrows(IllegalArgumentException.class, () -> categoryService.update(nullCategoryDTO, 1L));

        verifyNoInteractions(categoryRepository);
        verifyNoInteractions(categoryEntityValidator);
    }

    @Test
    public void update_whenRepositoryThrowsExceptionExtendsDataAccessException_throwServiceException() {
        doThrow(BadJpqlGrammarException.class).when(categoryRepository).findById(any());

        CategoryDTO categoryDTO = CategoryDTO.builder().id(1L).build();

        assertThrows(ServiceException.class, () -> categoryService.update(categoryDTO, 1L));

        verify(categoryRepository).findById(any());
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    public void update_whenValidatorThrowsValidationException_throwValidationException() {
        CategoryDTO categoryDTO = CategoryDTO.builder().id(1L).build();
        Category category = Category.builder().id(1L).build();

        when(categoryRepository.findById(any())).thenReturn(Optional.ofNullable(category));
        doThrow(ValidationException.class).when(categoryEntityValidator).validate(any());

        assertThrows(ValidationException.class, () -> categoryService.update(categoryDTO, 1L));

        verify(categoryEntityValidator).validate(any());

        verify(categoryRepository).findById(any());
        verifyNoMoreInteractions(categoryRepository);

        verify(categoryFromCategoryDTOUpdater).update(any(), any());
        verifyNoMoreInteractions(categoryFromCategoryDTOUpdater);

        verifyNoInteractions(converterService);
    }

    @Test
    public void update_whenEntityDoesNotExists_throwEntityNotFoundException() {
        when(categoryRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> categoryService.update(new CategoryDTO(), 1L));

        verify(categoryRepository).findById(any());
        verifyNoMoreInteractions(categoryRepository);

        verifyNoInteractions(categoryFromCategoryDTOUpdater);
        verifyNoInteractions(converterService);
        verifyNoInteractions(categoryEntityValidator);
    }

    @Test
    public void update_success() {
        CategoryDTO categoryDTO = CategoryDTO.builder().id(1L).build();
        Category category = Category.builder().id(1L).build();

        when(categoryRepository.findById(any())).thenReturn(Optional.ofNullable(category));
        when(categoryRepository.save(any())).thenReturn(category);
        when(converterService.convert(category, CategoryDTO.class)).thenReturn(categoryDTO);

        assertEquals(categoryDTO, categoryService.update(categoryDTO, 1L));

        verify(categoryRepository).findById(any());
        verify(categoryRepository).save(category);
        verifyNoMoreInteractions(categoryRepository);

        verify(categoryEntityValidator).validate(category);
        verifyNoMoreInteractions(categoryEntityValidator);

        verify(categoryFromCategoryDTOUpdater).update(categoryDTO, category);
        verifyNoMoreInteractions(categoryFromCategoryDTOUpdater);

        verify(converterService).convert(category, CategoryDTO.class);
        verifyNoMoreInteractions(converterService);
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

        assertThrows(EntityNotFoundException.class, () -> categoryService.deleteById(1L));

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

        Pageable pageable = mock(Pageable.class);
        when(pageable.getSort()).thenReturn(mock(org.springframework.data.domain.Sort.class));

        assertDoesNotThrow(() -> categoryService.findAll(pageable));

        verify(categoryRepository).findAll(any(Pageable.class));
    }

    @ParameterizedTest
    @NullSource
    public void findById_whenIdIsNull_throwIllegalArgumentException(Long nullId) {
        assertThrows(IllegalArgumentException.class, () -> categoryService.findById(nullId));
    }

    @Test
    public void findById_whenCategoryRepositoryThrowsExceptionExtendsDataAccessException_throwServiceException() {
        doThrow(BadJpqlGrammarException.class).when(categoryRepository).findById(any());

        assertThrows(ServiceException.class, () -> categoryService.findById(1L));

        verify(categoryRepository).findById(any());
    }

    @Test
    public void findById_success() {
        Category category = Category.builder().id(1L).build();
        CategoryDTO categoryDTO = CategoryDTO.builder().id(1L).build();

        when(categoryRepository.findById(any())).thenReturn(Optional.ofNullable(category));
        when(converterService.convert(category, CategoryDTO.class)).thenReturn(categoryDTO);

        assertEquals(categoryDTO, categoryService.findById(1L));

        verify(categoryRepository).findById(any());
        verifyNoMoreInteractions(categoryRepository);

        verify(converterService).convert(category, CategoryDTO.class);
        verifyNoMoreInteractions(converterService);
    }
}
