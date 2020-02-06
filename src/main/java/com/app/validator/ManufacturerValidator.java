package com.app.validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.entity.Manufacturer;
import com.app.repository.ManufacturerRepository;
import com.app.utils.Result;

@Component
public class ManufacturerValidator {
	
	@Autowired
	private ManufacturerRepository manufacturerRepository;
	
	@Autowired
	private VehicleModelValidator vehicleModelValidator;

	public Result checkBeforeSave(Manufacturer manufacturer) {
		Map<String,String> errors = validateManufacturerBeforeSave(manufacturer);
		
		if(errors.isEmpty())
			errors = vehicleModelValidator.checkBeforeSave(manufacturer.getVehicleModel());

		return Result.create(errors);
	}
	
	private Map<String, String> validateManufacturerBeforeSave(Manufacturer manufacturer) {
		Map<String,String> errors = new HashMap<>();
		
		List<Manufacturer> manufacturersWithSameName = manufacturerRepository.findByName(manufacturer.getName());
		
		if(manufacturer.getId() == null && manufacturersWithSameName.stream().filter(m -> m.getName().equals(manufacturer.getName())).findAny().isPresent()) {
			errors.put("manufacturer", "nameAlreadyExists");
		}
		else if(manufacturer.getId() != null && manufacturersWithSameName.stream().filter(m -> manufacturer.getId() != m.getId() && m.getName().equals(manufacturer.getName())).findAny().isPresent()) {
			errors.put("manufacturer", "nameAlreadyExists");
		}
		
		return errors;
	}
}
