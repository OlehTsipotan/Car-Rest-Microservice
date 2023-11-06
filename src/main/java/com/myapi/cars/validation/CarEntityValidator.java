package com.myapi.cars.validation;

import com.myapi.cars.exception.ValidationException;
import com.myapi.cars.model.Car;
import com.myapi.cars.repository.CarRepository;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CarEntityValidator extends EntityValidator<Car> {

    private final CarRepository carRepository;

    public CarEntityValidator(CarRepository carRepository, Validator validator) {
        super(validator);
        this.carRepository = carRepository;
    }

    @Override
    public void validate(Car car) {
        List<String> violations = new ArrayList<>();
        try {
            super.validate(car);
        } catch (ValidationException e) {
            violations = e.getViolations();
        }

        if (!violations.isEmpty()) {
            throw new ValidationException("Make is not valid", violations);
        }

    }
}
