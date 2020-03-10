package com.app.validator;

import com.app.entity.QAnnouncement;
import com.app.entity.VehicleModel;
import com.app.enums.ValidatorCode;
import com.app.repository.AnnouncementRepository;
import com.app.repository.VehicleModelRepository;
import com.app.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class VehicleModelValidator implements ValidatorCommonMethods<VehicleModel>  {
	
	@Autowired
	private VehicleModelRepository vehicleModelRepository;

	@Autowired
	private AnnouncementRepository announcementRepository;

/*	public Map<String, ValidatorCode> checkBeforeSave(List<VehicleModel> vehicleModels) {
		List<String> namesThatAlreadyExists = new ArrayList<>();
		
		for (VehicleModel vehicleModel : vehicleModels) {
			if(vehicleModel.getId() == null && vehicleModelRepository.countByName(vehicleModel.getName()) > 0) {
				namesThatAlreadyExists.add(vehicleModel.getName());
			}
			else if(vehicleModel.getId() != null && vehicleModelRepository.countByNameAndIdNot(vehicleModel.getName(), vehicleModel.getId()) > 0) {
				namesThatAlreadyExists.add(vehicleModel.getName());
			}
		}
		
		Map<String,ValidatorCode> errors = new HashMap<>();
		
	//	if(!namesThatAlreadyExists.isEmpty())
	//		errors.put("vehicleModelNameAlreadyExists", String.join(",", namesThatAlreadyExists));
		
		return errors;
	}*/

	@Override
	public Result checkBeforeSave(VehicleModel vehicleModel) {
		Map<String,ValidatorCode> errors = new HashMap<>();

		if(vehicleModel.getId() == null && vehicleModelRepository.countByName(vehicleModel.getName()) > 0) {
			errors.put("name",	ValidatorCode.ALREADY_EXISTS.setDetails(vehicleModel.getName()));
		}
		else if(vehicleModel.getId() != null && vehicleModelRepository.countByNameAndIdNot(vehicleModel.getName(), vehicleModel.getId()) > 0) {
			errors.put("name", ValidatorCode.ALREADY_EXISTS.setDetails(vehicleModel.getName()));
		}

		return Result.create(errors);
	}

	@Override
	public Result checkBeforeDelete(VehicleModel vehicleModel) {
		Map<String,ValidatorCode> errors = new HashMap<>();

		if(announcementRepository.countByPredicates(QAnnouncement.announcement.vehicleModel.eq(vehicleModel)) > 0)
			errors.put("announcements", ValidatorCode.HAVE_REF_OBJECTS.setDetails(vehicleModel.getId().toString()));

		return Result.create(errors);
	}
}
