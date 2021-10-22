package com.app.vehiclemodel.repository;

import com.app.common.enums.VehicleType;
import com.app.vehiclemodel.entity.VehicleModel;
import org.springframework.data.jpa.repository.Query;
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

    @Query(value = "SELECT name FROM VehicleModel where id = :id")
    String findNameById(Long id);
}
