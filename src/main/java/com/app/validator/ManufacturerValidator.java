package com.app.validator;

import com.app.entity.Manufacturer;
import com.app.entity.VehicleModel;
import com.app.enums.ValidatorCode;
import com.app.repository.ManufacturerRepository;
import com.app.utils.Result;
import com.app.utils.ValidationDetails;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ManufacturerValidator implements ValidatorCommonMethods<Manufacturer> {
	
	private ManufacturerRepository manufacturerRepository;
	
	private VehicleModelValidator vehicleModelValidator;


	public ManufacturerValidator(ManufacturerRepository manufacturerRepository, VehicleModelValidator vehicleModelValidator) {
		this.manufacturerRepository = manufacturerRepository;
		this.vehicleModelValidator = vehicleModelValidator;
	}

	public ManufacturerValidator() {
	}

	public Result checkBeforeSave(Manufacturer manufacturer) {
		Result result = Result.create(validateManufacturerBeforeSave(manufacturer));

		if(result.isSuccess()) {
			for (VehicleModel vehicleModel: manufacturer.getVehicleModel()) {
				result.appendResult(vehicleModelValidator.checkBeforeSave(vehicleModel));
			}
		}

		return result;
	}
	
	private Map<String, ValidationDetails> validateManufacturerBeforeSave(Manufacturer manufacturer) {
		Map<String, ValidationDetails> errors = new HashMap<>();
		
		List<Manufacturer> manufacturersWithSameName = manufacturerRepository.findByName(manufacturer.getName());
		
		if(manufacturer.getId() == null && manufacturersWithSameName.stream().anyMatch(m -> m.getName().equals(manufacturer.getName()))) {
			errors.put("name", ValidationDetails.of(ValidatorCode.ALREADY_EXISTS));
		}
		else if(manufacturer.getId() != null && manufacturersWithSameName.stream().anyMatch(m -> manufacturer.getId() != m.getId() && m.getName().equals(manufacturer.getName()))) {
			errors.put("name", ValidationDetails.of(ValidatorCode.ALREADY_EXISTS));
		}

		return errors;
	}

	public Result checkBeforeDelete(Manufacturer manufacturer) {
		Result result = Result.create(validateManufacturerBeforeSave(manufacturer));

		if(result.isSuccess()) {
			for (VehicleModel vehicleModel : manufacturer.getVehicleModel()) {
				result.appendResult(vehicleModelValidator.checkBeforeDelete(vehicleModel));
			}
		}

		return result;
	}

}
