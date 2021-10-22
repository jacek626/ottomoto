package com.app.searchform;

import com.app.manufacturer.dto.ManufacturerDto;
import com.app.manufacturer.entity.Manufacturer;
import com.app.manufacturer.repository.ManufacturerRepository;
import com.querydsl.core.types.Predicate;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
public class ManufacturerSearchStrategy implements SearchStrategy<Manufacturer, ManufacturerDto> {

    private final ManufacturerRepository manufacturerRepository;
    private final ModelMapper modelMapper;

    public ManufacturerSearchStrategy(ManufacturerRepository manufacturerRepository, ModelMapper modelMapper) {
        this.manufacturerRepository = manufacturerRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Page<Manufacturer> loadData(PageRequest pageRequest, Predicate predicate) {
        return manufacturerRepository.findAll(predicate, pageRequest);
    }

    @Override
    public ManufacturerDto convertToDto(Manufacturer entity) {
        return modelMapper.map(entity, ManufacturerDto.class);
    }
}
