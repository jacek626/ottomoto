package com.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.entity.Manufacturer;
import com.app.enums.VehicleType;
import com.app.projection.ManufacturerProjection;

@Repository
public interface ManufacturerRepository extends CrudRepository<Manufacturer, Long>, QuerydslPredicateExecutor<Manufacturer> {
	List<Manufacturer> findAll();
	List<Manufacturer> findByName(String name);
	
	@Query(value="select DISTINCT m.id as id, m.name as name from Manufacturer m join m.vehicleModel v where v.vehicleType = :vehicleType") 
	List<ManufacturerProjection> findByVehicleType(@Param("vehicleType") VehicleType vehicleType);
}
