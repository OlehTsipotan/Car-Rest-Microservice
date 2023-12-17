package com.myapi.cars.converter;

import com.myapi.cars.dto.CategoryDTO;
import com.myapi.cars.model.Category;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CategoryFromCategoryDTOUpdater {

    private final ModelMapper modelMapper;


    public CategoryFromCategoryDTOUpdater() {
        this.modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
    }

    public void update(CategoryDTO categoryDTO, Category category) {
        modelMapper.map(categoryDTO, category);
    }
}
