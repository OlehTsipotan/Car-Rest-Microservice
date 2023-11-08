package com.myapi.cars.converter;

import com.myapi.cars.dto.MakeDTO;
import com.myapi.cars.model.Make;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MakeDTOToMakeConverter implements Converter<MakeDTO, Make> {

    private final ModelMapper modelMapper;


    public MakeDTOToMakeConverter() {
        this.modelMapper = new ModelMapper();
    }

    @Override
    public Make convert(MakeDTO source) {
        return modelMapper.map(source, Make.class);
    }
}