package com.otomoto.repository;

import org.springframework.data.repository.CrudRepository;

import com.otomoto.entity.Manufacturer;

public interface ManufacturerRepository extends CrudRepository<Manufacturer, Long> {

}
