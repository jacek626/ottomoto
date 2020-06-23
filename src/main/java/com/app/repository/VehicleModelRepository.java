package com.app.repository;

import com.app.entity.VehicleModel;
import com.app.enums.VehicleType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleModelRepository extends CrudRepository<VehicleModel, Long> {

    List<VehicleModel> findByManufacturerId(Long manufacturerId);

    List<VehicleModel> findByManufacturerIdAndVehicleType(Long manufacturerId, VehicleType vehicleType);

    int countByName(String name);

    VehicleModel findByName(String name);

    int countByNameAndIdNot(String name, Long id);

}
