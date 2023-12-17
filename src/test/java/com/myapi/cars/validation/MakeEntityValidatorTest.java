package com.myapi.cars.validation;

import com.myapi.cars.exception.ValidationException;
import com.myapi.cars.model.Make;
import com.myapi.cars.repository.MakeRepository;
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
public class MakeEntityValidatorTest {

    private final Validator jakartaValidator = Validation.buildDefaultValidatorFactory().getValidator();
    private MakeEntityValidator validator;
    @Mock
    private MakeRepository makeRepository;

    @BeforeEach
    public void setUp() {
        validator = new MakeEntityValidator(makeRepository, jakartaValidator);
    }

    @Test
    public void validate_whenMakeIsValid() {
        Make make = new Make(1L, "name");
        assertDoesNotThrow(() -> validator.validate(make));
    }

    @ParameterizedTest
    @NullSource
    public void validate_whenMakeIsNull_throwIllegalArgumentException(Make nullMake) {
        assertThrows(IllegalArgumentException.class, () -> validator.validate(nullMake));
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void validate_whenMakeNameFieldIsEmptyOrNull_throwValidationException(String name) {
        Make make = new Make(1L, name);
        assertThrows(ValidationException.class, () -> validator.validate(make));
    }

    @Test
    public void validate_whenMakeNameIsNotUnique_throwValidationException() {
        String sharedName = "name";

        Make makeToValidate = new Make(1L, sharedName);
        Make makeToBeFounded = new Make(2L, sharedName);
        when(makeRepository.findByName(sharedName)).thenReturn(Optional.of(makeToBeFounded));
        assertThrows(ValidationException.class, () -> validator.validate(makeToValidate));
    }
}
