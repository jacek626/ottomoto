package com.app.repository;

import com.app.entity.Manufacturer;
import com.app.enums.VehicleType;
import com.app.projection.ManufacturerProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManufacturerRepository extends CrudRepository<Manufacturer, Long>, QuerydslPredicateExecutor<Manufacturer> {
	@NonNull
	@Override
	List<Manufacturer> findAll();
	List<Manufacturer> findByName(String name);
	
	@Query(value="select DISTINCT m.id as id, m.name as name from Manufacturer m join m.vehicleModel v where v.vehicleType = :vehicleType") 
	List<ManufacturerProjection> findByVehicleType(@Param("vehicleType") VehicleType vehicleType);
}
