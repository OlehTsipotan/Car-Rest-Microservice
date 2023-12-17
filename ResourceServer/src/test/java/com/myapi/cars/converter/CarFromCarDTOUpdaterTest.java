package com.myapi.cars.converter;

import com.myapi.cars.dto.CarDTO;
import com.myapi.cars.model.Car;
import com.myapi.cars.model.Category;
import com.myapi.cars.model.Make;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CarFromCarDTOUpdaterTest {

    private CarFromCarDTOUpdater carFromCarDTOUpdater;

    @BeforeEach
    public void setUp() {
        carFromCarDTOUpdater = new CarFromCarDTOUpdater();
    }

    @Test
    public void update_whenCarDTOIsValid_success() {
        Make make = Make.builder().id(1L).name("name").build();
        Category category = Category.builder().id(1L).name("name").build();
        Car car = Car.builder().id(1L).year(2020).make(make).model("model").categories(Set.of(category)).build();

        CarDTO carDTO = CarDTO.builder().model("newModel").build();

        carFromCarDTOUpdater.update(carDTO, car);

        assertEquals(carDTO.getModel(), car.getModel());
    }

    @ParameterizedTest
    @NullSource
    public void update_whenCarDTOIsNull_throwIllegalArgumentException(CarDTO nullCarDTO) {
        assertThrows(IllegalArgumentException.class, () -> carFromCarDTOUpdater.update(nullCarDTO, new Car()));
    }

    @ParameterizedTest
    @NullSource
    public void update_whenCarIsNull_throwIllegalArgumentException(Car nullCar) {
        assertThrows(IllegalArgumentException.class, () -> carFromCarDTOUpdater.update(new CarDTO(), nullCar));
    }


}
