package com.app.manufacturer.repository;

import com.app.announcement.types.VehicleType;
import com.app.manufacturer.entity.Manufacturer;
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

    @Query(value = "select DISTINCT m.id as id, m.name as name from Manufacturer m join m.vehicleModel v where v.vehicleType = :vehicleType")
    List<ManufacturerProjection> findByVehicleType(@Param("vehicleType") VehicleType vehicleType);

    @Query(value = "SELECT name FROM Manufacturer where id = :id")
    String findNameById(Long id);
}
