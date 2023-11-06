package com.myapi.cars.validation;

import com.myapi.cars.exception.ValidationException;
import com.myapi.cars.model.Category;
import com.myapi.cars.repository.CategoryRepository;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class CategoryEntityValidator extends EntityValidator<Category> {

    private final CategoryRepository categoryRepository;

    public CategoryEntityValidator(CategoryRepository categoryRepository, Validator validator) {
        super(validator);
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void validate(Category category) {
        List<String> violations = new ArrayList<>();
        try {
            super.validate(category);
        } catch (ValidationException e) {
            violations = e.getViolations();
        }

        Optional<Category> categoryToCheck = categoryRepository.findByName(category.getName());
        if (categoryToCheck.isPresent() && !category.equals(categoryToCheck.get())) {
            violations.add(String.format("Category with name = %s, already exists.", category.getName()));
        }

        if (!violations.isEmpty()) {
            throw new ValidationException("Make is not valid", violations);
        }

    }
}
