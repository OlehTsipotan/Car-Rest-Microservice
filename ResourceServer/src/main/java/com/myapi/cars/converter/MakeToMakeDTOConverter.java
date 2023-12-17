package com.myapi.cars.converter;

import com.myapi.cars.dto.MakeDTO;
import com.myapi.cars.model.Make;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MakeToMakeDTOConverter implements Converter<Make, MakeDTO> {

    private final ModelMapper modelMapper;


    public MakeToMakeDTOConverter() {
        this.modelMapper = new ModelMapper();
    }

    @Override
    public MakeDTO convert(Make source) {
        return modelMapper.map(source, MakeDTO.class);
    }
}
