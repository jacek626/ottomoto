package com.app.vehiclemodel.repository;

import com.app.vehiclemodel.entity.VehicleModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends CrudRepository<VehicleModel, Long> {

}
