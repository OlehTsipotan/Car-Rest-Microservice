package com.myapi.cars.converter;


import com.myapi.cars.dto.MakeDTO;
import com.myapi.cars.model.Make;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MakeFromMakeDTOUpdaterTest {

    private MakeFromMakeDTOUpdater makeFromMakeDTOUpdater;

    @BeforeEach
    public void setUp() {
        makeFromMakeDTOUpdater = new MakeFromMakeDTOUpdater();
    }

    @Test
    public void update_whenMakeDTOIsValid_success() {
        Make make = Make.builder().id(1L).name("name").build();
        MakeDTO makeDTO = MakeDTO.builder().name("newName").build();
        makeFromMakeDTOUpdater.update(makeDTO, make);

        assertEquals(makeDTO.getName(), make.getName());
    }

    @ParameterizedTest
    @NullSource
    public void update_whenMakeDTOIsNull_throwIllegalArgumentException(MakeDTO nullMakeDTO) {
        assertThrows(IllegalArgumentException.class, () -> makeFromMakeDTOUpdater.update(nullMakeDTO, new Make()));
    }

    @ParameterizedTest
    @NullSource
    public void update_whenMakeIsNull_throwIllegalArgumentException(Make nullMake) {
        assertThrows(IllegalArgumentException.class, () -> makeFromMakeDTOUpdater.update(new MakeDTO(), nullMake));
    }


}
