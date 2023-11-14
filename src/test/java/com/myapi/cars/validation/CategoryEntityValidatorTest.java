package com.myapi.cars.validation;

import com.myapi.cars.exception.ValidationException;
import com.myapi.cars.model.Category;
import com.myapi.cars.repository.CategoryRepository;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class CategoryEntityValidatorTest {

    private final Validator jakartaValidator = Validation.buildDefaultValidatorFactory().getValidator();
    private CategoryEntityValidator validator;
    @Mock
    private CategoryRepository categoryRepository;

    @BeforeEach
    public void setUp() {
        validator = new CategoryEntityValidator(categoryRepository, jakartaValidator);
    }

    @Test
    public void validate_whenCategoryIsValid() {
        Category category = new Category(1L, "name");
        assertDoesNotThrow(() -> validator.validate(category));
    }

    @ParameterizedTest
    @NullSource
    public void validate_whenCategoryIsNull_throwIllegalArgumentException(Category nullCategory) {
        assertThrows(IllegalArgumentException.class, () -> validator.validate(nullCategory));
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void validate_whenCategoryNameFieldIsEmptyOrNull_throwValidationException(String name) {
        Category category = new Category(1L, name);
        assertThrows(ValidationException.class, () -> validator.validate(category));
    }

    @Test
    public void validate_whenCategoryNameIsNotUnique_throwValidationException() {
        String sharedName = "name";

        Category categoryToValidate = new Category(1L, sharedName);
        Category categoryToBeFounded = new Category(2L, sharedName);
        when(categoryRepository.findByName(sharedName)).thenReturn(Optional.of(categoryToBeFounded));
        assertThrows(ValidationException.class, () -> validator.validate(categoryToValidate));
    }
}
