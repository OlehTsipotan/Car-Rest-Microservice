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
public class CarDTOToCarConverter implements Converter<CarDTO, Car> {

    private final ModelMapper modelMapper;
    private final CategoryDTOToCategoryConverter categoryDTOToCategoryConverter;


    public CarDTOToCarConverter() {
        this.modelMapper = new ModelMapper();
        this.categoryDTOToCategoryConverter = new CategoryDTOToCategoryConverter();

        org.modelmapper.Converter<Set<CategoryDTO>, Set<Category>> categoryListConverter =
                categoryList -> categoryList.getSource().stream().map(categoryDTOToCategoryConverter::convert)
                        .collect(Collectors.toSet());

        Condition notNull = ctx -> ctx.getSource() != null;

        modelMapper.typeMap(CarDTO.class, Car.class).addMappings(modelMapper -> {
            modelMapper.when(notNull).using(categoryListConverter).map(CarDTO::getCategories, Car::setCategories);
        });
    }

    @Override
    public Car convert(CarDTO source) {
        return modelMapper.map(source, Car.class);
    }
}
