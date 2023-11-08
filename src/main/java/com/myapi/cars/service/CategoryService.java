package com.myapi.cars.service;

import com.myapi.cars.converter.CategoryFromCategoryDTOUpdater;
import com.myapi.cars.dto.CategoryDTO;
import com.myapi.cars.dto.DTOSearchResponse;
import com.myapi.cars.exception.EntityAlreadyExistsException;
import com.myapi.cars.exception.EntityNotFoundException;
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

    private final ConverterService converterService;

    private final CategoryFromCategoryDTOUpdater categoryFromCategoryDTOUpdater;

    @Transactional
    public Long create(@NonNull CategoryDTO categoryDTO) {
        Category category = convertToEntity(categoryDTO);
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
    public CategoryDTO update(@NonNull CategoryDTO categoryDTO, @NonNull Long id) {
        Category categoryToUpdate = execute(() -> {
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("There is no Category to update with id = " + id));

            categoryFromCategoryDTOUpdater.update(categoryDTO, category);
            categoryEntityValidator.validate(category);

            return categoryRepository.save(category);
        });
        log.info("updated {}", categoryToUpdate);
        return convertToDTO(categoryToUpdate);
    }

    @Transactional
    public void deleteById(@NonNull Long id) {
        execute(() -> {
            if (!categoryRepository.existsById(id)) {
                throw new EntityNotFoundException("There is no Category to delete with id = " + id);
            }
            categoryRepository.deleteById(id);
        });
        log.info("Deleted id = {}", id);
    }

    public DTOSearchResponse findAllAsDTO(@NonNull Pageable pageable) {
        List<CategoryDTO> categoryDTOList =
                execute(() -> categoryRepository.findAll(pageable)).stream().map(this::convertToDTO).toList();
        log.debug("Retrieved All {} Categorys", categoryDTOList.size());
        return DTOSearchResponse.builder().offset(pageable.getOffset()).limit(pageable.getPageSize())
                .total(categoryDTOList.size()).sort(pageable.getSort().toString()).data(categoryDTOList).build();
    }

    public CategoryDTO findByIdAsDTO(@NonNull Long id) {
        Category category = execute(() -> categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("There is no Category with id = " + id)));
        log.debug("Retrieved Category by id = {}", id);
        return convertToDTO(category);
    }

    private CategoryDTO convertToDTO(Category category) {
        return converterService.convert(category, CategoryDTO.class);
    }

    private Category convertToEntity(CategoryDTO categoryDTO) {
        return converterService.convert(categoryDTO, Category.class);
    }

    private <T> T execute(DaoSupplier<T> supplier) {
        try {
            return supplier.get();
        } catch (DataAccessException e) {
            throw new ServiceException("DAO operation failed", e);
        }
    }

    private void execute(DaoProcessor processor) {
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
