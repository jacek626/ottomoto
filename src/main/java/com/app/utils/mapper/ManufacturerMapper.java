package com.app.utils.mapper;

import com.app.dto.ManufacturerDto;
import com.app.entity.Manufacturer;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ManufacturerMapper {

    private final ModelMapper modelMapper;

    public ManufacturerMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ManufacturerDto convertToDto(Manufacturer manufacturer) {
        ManufacturerDto manufacturerDto = modelMapper.map(manufacturer, ManufacturerDto.class);

        return manufacturerDto;
    }

    public Manufacturer convertToEntity(ManufacturerDto manufacturerDto) {
        Manufacturer manufacturer = modelMapper.map(manufacturerDto, Manufacturer.class);

        return manufacturer;
    }
}
