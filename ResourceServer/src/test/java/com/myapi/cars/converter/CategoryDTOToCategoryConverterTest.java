package com.myapi.cars.converter;

import com.myapi.cars.dto.CategoryDTO;
import com.myapi.cars.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CategoryDTOToCategoryConverterTest {

    private CategoryDTOToCategoryConverter categoryDTOToCategoryConverter;

    @BeforeEach
    public void setUp() {
        categoryDTOToCategoryConverter = new CategoryDTOToCategoryConverter();
    }

    @Test
    public void convert_whenCategoryDTOIsValid_success() {
        CategoryDTO categoryDTO = CategoryDTO.builder().id(1L).name("name").build();
        Category category = Category.builder().id(1L).name("name").build();
        assertEquals(category, categoryDTOToCategoryConverter.convert(categoryDTO));
    }

    @ParameterizedTest
    @NullSource
    public void convert_whenCategoryDTOIsNull_throwIllegalArgumentException(CategoryDTO nullCategoryDTO) {
        assertThrows(IllegalArgumentException.class,
                () -> categoryDTOToCategoryConverter.convert(nullCategoryDTO));
    }
}