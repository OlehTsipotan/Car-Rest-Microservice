package com.myapi.cars.converter;

import com.myapi.cars.dto.MakeDTO;
import com.myapi.cars.model.Make;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MakeFromMakeDTOUpdater {

    private final ModelMapper modelMapper;


    public MakeFromMakeDTOUpdater() {
        this.modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
    }

    public void update(MakeDTO makeDTO, Make make) {
        modelMapper.map(makeDTO, make);
    }
}
