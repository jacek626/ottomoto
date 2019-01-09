package com.otomoto.repository;

import org.springframework.data.repository.CrudRepository;

import com.otomoto.entity.VehicleModel;

public interface VehicleRepository extends CrudRepository<VehicleModel, Long> {

}
