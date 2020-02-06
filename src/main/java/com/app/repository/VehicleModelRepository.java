package com.app.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.app.entity.VehicleModel;
import com.app.enums.VehicleType;

@Repository
public interface VehicleModelRepository extends CrudRepository<VehicleModel, Long> {
	
	List<VehicleModel> findByManufacturerId(Long manufacturerId);
	List<VehicleModel> findByManufacturerIdAndVehicleType(Long manufacturerId, VehicleType vehicleType);
	
	int countByName(String name);
	int countByNameAndIdNot(String name, Long id);

}
