package com.app.searchform;

import com.app.entity.Manufacturer;
import com.app.repository.ManufacturerRepository;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
public class ManufacturerSearchStrategy implements SearchStrategy<Manufacturer> {

    private final ManufacturerRepository manufacturerRepository;

    public ManufacturerSearchStrategy(ManufacturerRepository manufacturerRepository) {
        this.manufacturerRepository = manufacturerRepository;
    }

    @Override
    public Page<Manufacturer> loadData(PageRequest pageRequest, Predicate predicate) {
        return manufacturerRepository.findAll(predicate, pageRequest);
    }
}
