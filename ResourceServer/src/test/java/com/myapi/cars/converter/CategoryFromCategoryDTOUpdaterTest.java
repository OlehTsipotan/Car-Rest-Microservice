package com.myapi.cars.converter;

import com.myapi.cars.dto.CategoryDTO;
import com.myapi.cars.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CategoryFromCategoryDTOUpdaterTest {

    private CategoryFromCategoryDTOUpdater categoryFromCategoryDTOUpdater;

    @BeforeEach
    public void setUp() {
        categoryFromCategoryDTOUpdater = new CategoryFromCategoryDTOUpdater();
    }

    @Test
    public void update_whenCategoryDTOIsValid_success() {
        Category category = Category.builder().id(1L).name("name").build();
        CategoryDTO categoryDTO = CategoryDTO.builder().name("newName").build();
        categoryFromCategoryDTOUpdater.update(categoryDTO, category);

        assertEquals(categoryDTO.getName(), category.getName());
    }

    @ParameterizedTest
    @NullSource
    public void update_whenCategoryDTOIsNull_throwIllegalArgumentException(CategoryDTO nullCategoryDTO) {
        assertThrows(IllegalArgumentException.class,
                () -> categoryFromCategoryDTOUpdater.update(nullCategoryDTO, new Category()));
    }

    @ParameterizedTest
    @NullSource
    public void update_whenCategoryIsNull_throwIllegalArgumentException(Category nullCategory) {
        assertThrows(IllegalArgumentException.class,
                () -> categoryFromCategoryDTOUpdater.update(new CategoryDTO(), nullCategory));
    }


}