package com.myapi.cars.validation;

import com.myapi.cars.exception.FieldViolation;
import com.myapi.cars.exception.ValidationException;
import com.myapi.cars.model.Make;
import com.myapi.cars.repository.MakeRepository;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class MakeEntityValidator extends EntityValidator<Make> {

    private final MakeRepository makeRepository;

    public MakeEntityValidator(MakeRepository makeRepository, Validator validator) {
        super(validator);
        this.makeRepository = makeRepository;
    }

    @Override
    public void validate(Make make) {
        List<FieldViolation> violations = new ArrayList<>();
        try {
            super.validate(make);
        } catch (ValidationException e) {
            violations = e.getViolations();
        }

        Optional<Make> makeToCheck = makeRepository.findByName(make.getName());
        if (makeToCheck.isPresent() && !make.equals(makeToCheck.get())) {
            FieldViolation fieldViolation =
                    new FieldViolation("name", make.getClass().getSimpleName(), make.getName(),
                            String.format("Make with name = %s, already exists.", make.getName()));
            violations.add(fieldViolation);
        }

        if (!violations.isEmpty()) {
            throw new ValidationException("Make is not valid", violations);
        }

    }
}
