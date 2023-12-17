package com.myapi.cars.converter;

import com.myapi.cars.dto.CategoryDTO;
import com.myapi.cars.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CategoryToCategoryDTOConverterTest {

    private CategoryToCategoryDTOConverter categoryToCategoryDTOConverter;

    @BeforeEach
    public void setUp() {
        categoryToCategoryDTOConverter = new CategoryToCategoryDTOConverter();
    }

    @Test
    public void convert_whenCategoryIsValid_success() {
        CategoryDTO categoryDTO = CategoryDTO.builder().id(1L).name("name").build();
        Category category = Category.builder().id(1L).name("name").build();
        assertEquals(categoryDTO, categoryToCategoryDTOConverter.convert(category));
    }

    @ParameterizedTest
    @NullSource
    public void convert_whenCategoryIsNull_throwIllegalArgumentException(Category nullCategory) {
        assertThrows(IllegalArgumentException.class,
                () -> categoryToCategoryDTOConverter.convert(nullCategory));
    }
}
