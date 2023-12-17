package com.myapi.cars.converter;

import com.myapi.cars.dto.CarDTO;
import com.myapi.cars.dto.CategoryDTO;
import com.myapi.cars.dto.MakeDTO;
import com.myapi.cars.model.Car;
import com.myapi.cars.model.Category;
import com.myapi.cars.model.Make;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CarDTOToCarConverterTest {

    private CarDTOToCarConverter carDTOToCarEntityConverter;

    @BeforeEach
    public void setUp() {
        carDTOToCarEntityConverter = new CarDTOToCarConverter();
    }

    @ParameterizedTest
    @NullSource
    public void convert_whenCarDTOIsNull_throwIllegalArgumentException(CarDTO nullCarDTO) {
        assertThrows(IllegalArgumentException.class, () -> carDTOToCarEntityConverter.convert(nullCarDTO));
    }

    @Test
    public void convert_whenCarDTOFieldsAreNull_success() {
        CarDTO carDTO = CarDTO.builder().build();
        Car car = new Car();

        Car carConverted = carDTOToCarEntityConverter.convert(carDTO);

        assertEquals(car.getId(), carConverted.getId());
        assertEquals(car.getYear(), carConverted.getYear());
        assertEquals(car.getMake(), carConverted.getMake());
        assertEquals(car.getModel(), carConverted.getModel());
        assertEquals(car.getCategories(), carConverted.getCategories());
    }

    @Test
    public void convert_whenCarDTOCoursesAreEmptyList_success() {
        CarDTO carDTO = CarDTO.builder().categories(new HashSet<>()).build();
        Car car = new Car();

        Car carConverted = carDTOToCarEntityConverter.convert(carDTO);

        assertEquals(car.getId(), carConverted.getId());
        assertEquals(car.getYear(), carConverted.getYear());
        assertEquals(car.getMake(), carConverted.getMake());
        assertEquals(car.getModel(), carConverted.getModel());
        assertEquals(car.getCategories(), carConverted.getCategories());
    }

    @Test
    public void convert_success() {
        MakeDTO makeDTO = MakeDTO.builder().id(1L).name("name").build();
        CategoryDTO categoryDTO = CategoryDTO.builder().id(1L).name("name").build();
        CarDTO carDTO = CarDTO.builder().id(1L).year(2020).make(makeDTO).model("model").categories(Set.of(categoryDTO)).build();

        Make make = Make.builder().id(1L).name("name").build();
        Category category = Category.builder().id(1L).name("name").build();
        Car car = Car.builder().id(1L).year(2020).make(make).model("model").categories(Set.of(category)).build();

        Car carConverted = carDTOToCarEntityConverter.convert(carDTO);

        assertEquals(car.getId(), carConverted.getId());
        assertEquals(car.getYear(), carConverted.getYear());
        assertEquals(car.getMake(), carConverted.getMake());
        assertEquals(car.getModel(), carConverted.getModel());
        assertEquals(car.getCategories(), carConverted.getCategories());

    }
}
