package com.myapi.cars.converter;

import com.myapi.cars.dto.CategoryDTO;
import com.myapi.cars.model.Category;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CategoryDTOToCategoryConverter implements Converter<CategoryDTO, Category> {

    private final ModelMapper modelMapper;


    public CategoryDTOToCategoryConverter() {
        this.modelMapper = new ModelMapper();
    }

    @Override
    public Category convert(CategoryDTO source) {
        return modelMapper.map(source, Category.class);
    }
}
