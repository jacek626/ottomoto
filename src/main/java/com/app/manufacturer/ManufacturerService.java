package com.app.manufacturer;

import com.app.common.utils.validation.Result;
import com.app.manufacturer.entity.Manufacturer;
import com.app.manufacturer.repository.ManufacturerRepository;
import com.app.manufacturer.validator.ManufacturerValidator;
import com.app.vehiclemodel.entity.VehicleModel;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@AllArgsConstructor
public class ManufacturerService {
    private final ManufacturerRepository manufacturerRepository;
    private final ManufacturerValidator manufacturerValidator;

	public Result<Manufacturer> saveManufacturer(Manufacturer manufacturer) {
        var result = manufacturerValidator.validateForSave(manufacturer);

        for (VehicleModel vehicleModel : manufacturer.getVehicleModelAsOptional().orElse(Collections.emptyList())) {
            vehicleModel.setManufacturer(manufacturer);
        }

        result.ifSuccess(() -> manufacturerRepository.save(manufacturer));

        return result;
    }

    public Manufacturer addVehicle(Manufacturer manufacturer) {
        var vehicleModel = new VehicleModel();
        vehicleModel.setManufacturer(manufacturer);

        manufacturer.getVehicleModelAsOptional().
                ifPresentOrElse(e -> e.add(vehicleModel), () -> {
                    manufacturer.setVehicleModel(Lists.newArrayList());
                    manufacturer.getVehicleModel().add(vehicleModel);
        });

        return manufacturer;
    }

    public Result<Manufacturer> deleteManufacturer(Manufacturer manufacturer) {
        return manufacturerValidator.validateForDelete(manufacturer)
                .ifSuccess(() -> manufacturerRepository.delete(manufacturer));
    }
	

}
