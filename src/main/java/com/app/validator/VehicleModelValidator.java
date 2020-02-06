package com.app.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.entity.VehicleModel;
import com.app.repository.VehicleModelRepository;

@Component
public class VehicleModelValidator {
	
	@Autowired
	private VehicleModelRepository vehicleModelRepository;

	Map<String, String> checkBeforeSave(List<VehicleModel> vehicleModels) {
		List<String> namesThatAlreadyExists = new ArrayList<>();
		
		for (VehicleModel vehicleModel : vehicleModels) {
			if(vehicleModel.getId() == null && vehicleModelRepository.countByName(vehicleModel.getName()) > 0) {
				namesThatAlreadyExists.add(vehicleModel.getName());
			}
			else if(vehicleModel.getId() != null && vehicleModelRepository.countByNameAndIdNot(vehicleModel.getName(), vehicleModel.getId()) > 0) {
				namesThatAlreadyExists.add(vehicleModel.getName());
			}
		}
		
		Map<String,String> errors = new HashMap<>();
		
		if(!namesThatAlreadyExists.isEmpty())
			errors.put("vehicleModelNameAlreadyExists", String.join(",", namesThatAlreadyExists));
		
		return errors;
	}
}
