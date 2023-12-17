package com.myapi.cars.converter;

import com.myapi.cars.dto.CarDTO;
import com.myapi.cars.dto.CategoryDTO;
import com.myapi.cars.model.Car;
import com.myapi.cars.model.Category;
import org.modelmapper.Condition;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CarFromCarDTOUpdater {

    private final ModelMapper modelMapper;

    private final CategoryDTOToCategoryConverter categoryDTOToCategoryConverter;

    public CarFromCarDTOUpdater() {
        this.modelMapper = new ModelMapper();
        this.categoryDTOToCategoryConverter = new CategoryDTOToCategoryConverter();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        org.modelmapper.Converter<List<CategoryDTO>, Set<Category>> categoryListConverter =
                categoryList -> categoryList.getSource().stream().map(categoryDTOToCategoryConverter::convert)
                        .collect(Collectors.toSet());

        Condition notNull = ctx -> ctx.getSource() != null;

        modelMapper.typeMap(CarDTO.class, Car.class).addMappings(modelMapper -> {
            modelMapper.when(notNull).using(categoryListConverter).map(CarDTO::getCategories, Car::setCategories);
        });
    }

    public void update(CarDTO carDTO, Car car) {
        modelMapper.map(carDTO, car);
    }
}