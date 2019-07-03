package com.otomoto.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.otomoto.entity.VehicleModel;

@Repository
public interface VehicleRepository extends CrudRepository<VehicleModel, Long> {

}
