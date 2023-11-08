package com.myapi.cars.converter;

import com.myapi.cars.dto.MakeDTO;
import com.myapi.cars.model.Make;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MakeToMakeDTOConverterTest {

    private MakeToMakeDTOConverter makeToMakeDTOConverter;

    @BeforeEach
    public void setUp() {
        makeToMakeDTOConverter = new MakeToMakeDTOConverter();
    }

    @Test
    public void convert_whenMakeIsValid_success() {
        MakeDTO makeDTO = MakeDTO.builder().id(1L).name("name").build();
        Make make = Make.builder().id(1L).name("name").build();
        assertEquals(makeDTO, makeToMakeDTOConverter.convert(make));
    }

    @ParameterizedTest
    @NullSource
    public void convert_whenMakeIsNull_throwIllegalArgumentException(Make nullMake) {
        assertThrows(IllegalArgumentException.class,
                () -> makeToMakeDTOConverter.convert(nullMake));
    }
}
