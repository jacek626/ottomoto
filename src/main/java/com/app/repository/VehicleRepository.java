package com.app.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.app.entity.VehicleModel;

@Repository
public interface VehicleRepository extends CrudRepository<VehicleModel, Long> {

}
