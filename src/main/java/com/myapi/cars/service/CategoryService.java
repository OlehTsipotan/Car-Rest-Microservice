package com.myapi.cars.service;

import com.myapi.cars.exception.EntityAlreadyExistsException;
import com.myapi.cars.exception.EntityDoesNotExistsException;
import com.myapi.cars.exception.ServiceException;
import com.myapi.cars.model.Category;
import com.myapi.cars.repository.CategoryRepository;
import com.myapi.cars.validation.CategoryEntityValidator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryEntityValidator categoryEntityValidator;

    @Transactional
    public Long create(@NonNull Category category) {
        execute(() -> {
            categoryEntityValidator.validate(category);
            if (category.getId() != null && categoryRepository.existsById(category.getId())) {
                throw new EntityAlreadyExistsException("Category with id = " + category.getId() + " already exists");
            }
            categoryRepository.save(category);
        });
        log.info("saved {}", category);
        return category.getId();
    }

    @Transactional
    public Long update(@NonNull Category category) {
        execute(() -> {
            categoryEntityValidator.validate(category);
            if (category.getId() == null || !categoryRepository.existsById(category.getId())) {
                throw new EntityDoesNotExistsException("Category with id = " + category.getId() + " do not exists");
            }
            categoryRepository.save(category);
        });
        log.info("updated {}", category);
        return category.getId();
    }

    @Transactional
    public void deleteById(@NonNull Long id) {
        execute(() -> {
            if (!categoryRepository.existsById(id)) {
                throw new EntityDoesNotExistsException("There is no Category to delete with id = " + id);
            }
            categoryRepository.deleteById(id);
        });
        log.info("Deleted id = {}", id);
    }

    public List<Category> findAll(@NonNull Pageable pageable) {
        List<Category> categories = execute(() -> categoryRepository.findAll(pageable)).stream().toList();
        log.debug("Retrieved All {} Categories", categories.size());
        return categories;
    }

    private <T> T execute(com.myapi.cars.service.MakeService.DaoSupplier<T> supplier) {
        try {
            return supplier.get();
        } catch (DataAccessException e) {
            throw new ServiceException("DAO operation failed", e);
        }
    }

    private void execute(com.myapi.cars.service.MakeService.DaoProcessor processor) {
        try {
            processor.process();
        } catch (DataAccessException e) {
            throw new ServiceException("DAO operation failed", e);
        }
    }

    @FunctionalInterface
    public interface DaoSupplier<T> {
        T get();
    }

    @FunctionalInterface
    public interface DaoProcessor {
        void process();
    }
}