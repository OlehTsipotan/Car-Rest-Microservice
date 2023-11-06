package com.myapi.cars.validation;

import com.myapi.cars.model.Car;
import com.myapi.cars.model.Category;
import com.myapi.cars.model.Make;
import com.myapi.cars.repository.CarRepository;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
public class CarEntityValidatorTest {

    private final Validator jakartaValidator = Validation.buildDefaultValidatorFactory().getValidator();
    private CarEntityValidator validator;
    @Mock
    private CarRepository carRepository;

    @BeforeEach
    public void setUp() {
        validator = new CarEntityValidator(carRepository, jakartaValidator);
    }

    @Test
    public void validate_whenCarIsValid() {
        Category category = new Category(1L, "name");
        Make make = new Make(1L, "name");
        Car car = Car.builder().id(UUID.randomUUID()).year(2003).model("some Model").categories(Set.of(category))
                .make(make).build();
        assertDoesNotThrow(() -> validator.validate(car));
    }

    @ParameterizedTest
    @NullSource
    public void validate_whenCarIsNull_throwIllegalArgumentException(Car nullCar) {
        assertThrows(IllegalArgumentException.class, () -> validator.validate(nullCar));
    }
}
