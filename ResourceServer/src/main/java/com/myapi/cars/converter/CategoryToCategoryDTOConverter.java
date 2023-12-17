package com.myapi.cars.converter;

import com.myapi.cars.dto.CategoryDTO;
import com.myapi.cars.model.Category;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CategoryToCategoryDTOConverter implements Converter<Category, CategoryDTO> {

    private final ModelMapper modelMapper;


    public CategoryToCategoryDTOConverter() {
        this.modelMapper = new ModelMapper();
    }

    @Override
    public CategoryDTO convert(Category source) {
        return modelMapper.map(source, CategoryDTO.class);
    }
}
