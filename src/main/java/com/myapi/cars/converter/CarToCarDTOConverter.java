package com.myapi.cars.converter;

import com.myapi.cars.dto.CarDTO;
import com.myapi.cars.dto.CategoryDTO;
import com.myapi.cars.model.Car;
import com.myapi.cars.model.Category;
import org.modelmapper.Condition;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CarToCarDTOConverter implements Converter<Car, CarDTO> {

    private final ModelMapper modelMapper;

    private final CategoryToCategoryDTOConverter categoryToCategoryDTOConverter;


    public CarToCarDTOConverter() {
        this.modelMapper = new ModelMapper();
        this.categoryToCategoryDTOConverter = new CategoryToCategoryDTOConverter();

        org.modelmapper.Converter<Set<Category>, Set<CategoryDTO>> categorySetConverter =
                categoryList -> categoryList.getSource().stream().map(categoryToCategoryDTOConverter::convert).collect(
                        Collectors.toSet());

        Condition notNull = ctx -> ctx.getSource() != null;

        modelMapper.typeMap(Car.class, CarDTO.class).addMappings(modelMapper -> {
            modelMapper.when(notNull).using(categorySetConverter).map(Car::getCategories, CarDTO::setCategories);
        });
    }

    @Override
    public CarDTO convert(Car source) {
        return modelMapper.map(source, CarDTO.class);
    }
}