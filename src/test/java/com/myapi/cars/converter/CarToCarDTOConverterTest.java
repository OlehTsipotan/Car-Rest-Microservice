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

public class CarToCarDTOConverterTest {

    private CarToCarDTOConverter carToCarDTOConverter;

    @BeforeEach
    public void setUp() {
        carToCarDTOConverter = new CarToCarDTOConverter();
    }

    @ParameterizedTest
    @NullSource
    public void convert_whenCarIsNull_throwIllegalArgumentException(Car nullCarDTO) {
        assertThrows(IllegalArgumentException.class, () -> carToCarDTOConverter.convert(nullCarDTO));
    }

    @Test
    public void convert_whenCarDTOFieldsAreNull_success() {
        CarDTO carDTO = CarDTO.builder().categories(new HashSet<>()).build();
        Car car = new Car();

        CarDTO carConverted = carToCarDTOConverter.convert(car);

        assertEquals(carDTO.getId(), carConverted.getId());
        assertEquals(carDTO.getYear(), carConverted.getYear());
        assertEquals(carDTO.getMake(), carConverted.getMake());
        assertEquals(carDTO.getModel(), carConverted.getModel());
        assertEquals(carDTO.getCategories(), carConverted.getCategories());
    }

    @Test
    public void convert_whenCarDTOCoursesAreEmptyList_success() {
        CarDTO carDTO = CarDTO.builder().categories(new HashSet<>()).build();
        Car car = new Car();

        CarDTO carConverted = carToCarDTOConverter.convert(car);

        assertEquals(carDTO.getId(), carConverted.getId());
        assertEquals(carDTO.getYear(), carConverted.getYear());
        assertEquals(carDTO.getMake(), carConverted.getMake());
        assertEquals(carDTO.getModel(), carConverted.getModel());
        assertEquals(carDTO.getCategories(), carConverted.getCategories());
    }

    @Test
    public void convert_success() {
        MakeDTO makeDTO = MakeDTO.builder().id(1L).name("name").build();
        CategoryDTO categoryDTO = CategoryDTO.builder().id(1L).name("name").build();
        CarDTO carDTO = CarDTO.builder().id(1L).year(2020).make(makeDTO).model("model").categories(Set.of(categoryDTO)).build();

        Make make = Make.builder().id(1L).name("name").build();
        Category category = Category.builder().id(1L).name("name").build();
        Car car = Car.builder().id(1L).year(2020).make(make).model("model").categories(Set.of(category)).build();

        CarDTO carConverted = carToCarDTOConverter.convert(car);

        assertEquals(carDTO.getId(), carConverted.getId());
        assertEquals(carDTO.getYear(), carConverted.getYear());
        assertEquals(carDTO.getMake(), carConverted.getMake());
        assertEquals(carDTO.getModel(), carConverted.getModel());
        assertEquals(carDTO.getCategories(), carConverted.getCategories());

    }
}
