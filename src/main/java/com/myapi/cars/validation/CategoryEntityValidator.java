package com.myapi.cars.validation;

import com.myapi.cars.exception.FieldViolation;
import com.myapi.cars.exception.ValidationException;
import com.myapi.cars.model.Category;
import com.myapi.cars.repository.CategoryRepository;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class CategoryEntityValidator extends EntityValidator<Category> {

    private final CategoryRepository categoryRepository;

    public CategoryEntityValidator(CategoryRepository categoryRepository, Validator validator) {
        super(validator);
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void validate(Category category) {
        List<FieldViolation> violations = new ArrayList<>();
        try {
            super.validate(category);
        } catch (ValidationException e) {
            violations = e.getViolations();
        }

        Optional<Category> categoryToCheck = categoryRepository.findByName(category.getName());
        if (categoryToCheck.isPresent() && !category.equals(categoryToCheck.get())) {
            FieldViolation fieldViolation =
                    new FieldViolation("name", category.getClass().getSimpleName(), category.getName(),
                            String.format("Category with name = %s, already exists.", category.getName()));
            violations.add(fieldViolation);
        }

        if (!violations.isEmpty()) {
            log.info(violations.toString());
            throw new ValidationException("Category is not valid", violations);
        }

    }
}
