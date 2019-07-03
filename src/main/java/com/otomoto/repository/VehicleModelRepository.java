package com.otomoto.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.otomoto.entity.VehicleModel;
import com.otomoto.enums.VehicleType;

@Repository
public interface VehicleModelRepository extends CrudRepository<VehicleModel, Long> {
	
	List<VehicleModel> findByManufacturerId(Long manufacturerId);
	List<VehicleModel> findByManufacturerIdAndVehicleType(Long manufacturerId, VehicleType vehicleType);

}
