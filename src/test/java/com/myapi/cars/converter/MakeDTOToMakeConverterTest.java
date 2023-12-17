package com.myapi.cars.converter;

import com.myapi.cars.dto.MakeDTO;
import com.myapi.cars.model.Make;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MakeDTOToMakeConverterTest {

    private MakeDTOToMakeConverter makeDTOToMakeConverter;

    @BeforeEach
    public void setUp() {
        makeDTOToMakeConverter = new MakeDTOToMakeConverter();
    }

    @Test
    public void convert_whenMakeDTOIsValid_success() {
        MakeDTO makeDTO = MakeDTO.builder().id(1L).name("name").build();
        Make make = Make.builder().id(1L).name("name").build();
        assertEquals(make, makeDTOToMakeConverter.convert(makeDTO));
    }

    @ParameterizedTest
    @NullSource
    public void convert_whenMakeDTOIsNull_throwIllegalArgumentException(MakeDTO nullMakeDTO) {
        assertThrows(IllegalArgumentException.class,
                () -> makeDTOToMakeConverter.convert(nullMakeDTO));
    }
}
