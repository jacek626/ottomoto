package com.app.manufacturer;

import com.app.common.utils.validation.Result;
import com.app.manufacturer.entity.Manufacturer;
import com.app.manufacturer.repository.ManufacturerRepository;
import com.app.manufacturer.validator.ManufacturerValidator;
import com.app.vehiclemodel.entity.VehicleModel;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service("manufacturerService")
public class ManufacturerService {

    private final ManufacturerRepository manufacturerRepository;

    private final ManufacturerValidator manufacturerValidator;


    public ManufacturerService(ManufacturerRepository manufacturerRepository, ManufacturerValidator manufacturerValidator) {
		this.manufacturerRepository = manufacturerRepository;
		this.manufacturerValidator = manufacturerValidator;
	}


	public Result saveManufacturer(Manufacturer manufacturer) {
        Result result = manufacturerValidator.checkBeforeSave(manufacturer);

        for (VehicleModel vehicleModel : manufacturer.getVehicleModelAsOptional().orElse(Collections.emptyList())) {
            vehicleModel.setManufacturer(manufacturer);
        }

        result.ifSuccess(() -> manufacturerRepository.save(manufacturer));

        return result;
    }

    public Manufacturer addVehicle(Manufacturer manufacturer) {
        VehicleModel vehicleModel = new VehicleModel();
        vehicleModel.setManufacturer(manufacturer);

        manufacturer.getVehicleModelAsOptional().ifPresentOrElse(e -> {
            e.add(vehicleModel);
        }, () -> {
            manufacturer.setVehicleModel(Lists.newArrayList());
            manufacturer.getVehicleModel().add(vehicleModel);
        });

        return manufacturer;
    }

    public Result deleteManufacturer(Manufacturer manufacturer) {
        Result result = manufacturerValidator.checkBeforeDelete(manufacturer);

        if (result.isSuccess())
            manufacturerRepository.delete(manufacturer);

        return result;
    }
	

}
