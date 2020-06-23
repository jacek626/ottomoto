package com.app.validator;

import com.app.entity.VehicleModel;
import com.app.enums.ValidatorCode;
import com.app.repository.AnnouncementRepository;
import com.app.repository.VehicleModelRepository;
import com.app.utils.Result;
import com.app.utils.ValidationDetails;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class VehicleModelValidator implements ValidatorCommonMethods<VehicleModel> {

    private final VehicleModelRepository vehicleModelRepository;

    private final AnnouncementRepository announcementRepository;

    public VehicleModelValidator(VehicleModelRepository vehicleModelRepository, AnnouncementRepository announcementRepository) {
        this.vehicleModelRepository = vehicleModelRepository;
        this.announcementRepository = announcementRepository;
    }

    @Override
    public Result checkBeforeSave(VehicleModel vehicleModel) {
        var errors = createErrorMap();

        errors.putAll(checkNameIsNotEmpty(vehicleModel.getName()));
        errors.putAll(checkNameIsUnique(vehicleModel));

        return Result.create(errors);
    }

    private Map<String, ValidationDetails> checkNameIsUnique(VehicleModel vehicleModel) {
        var errors = createErrorMap();

        if (vehicleModel.getId() == null && vehicleModelRepository.countByName(vehicleModel.getName()) > 0) {
            errors.put("VehicleModelName", ValidationDetails.of(ValidatorCode.ALREADY_EXISTS, vehicleModel.getName()).appendDetail(vehicleModel.getName()).objectName("VehicleModel"));
        } else if (vehicleModel.getId() != null && vehicleModelRepository.countByNameAndIdNot(vehicleModel.getName(), vehicleModel.getId()) > 0) {
            errors.put("VehicleModelName", ValidationDetails.of(ValidatorCode.ALREADY_EXISTS, vehicleModel.getName()).appendDetail(vehicleModel.getName()).objectName("VehicleModel"));
        }

        return errors;
    }

    private Map<String, ValidationDetails> checkNameIsNotEmpty(String vehicleName) {
        var errors = createErrorMap();

        if (StringUtils.isBlank(vehicleName)) {
            errors.put("VehicleModelName", ValidationDetails.of(ValidatorCode.IS_EMPTY, "Nazwa modelu").objectName("VehicleModel"));
        }

        return errors;
    }

    @Override
    public Result checkBeforeDelete(VehicleModel vehicleModel) {
        var errors = createErrorMap();

        errors.putAll(checkIsAnnouncementsWithThisVehicleModelExists(vehicleModel));

        return Result.create(errors);
    }

    private Map<String, ValidationDetails> checkIsAnnouncementsWithThisVehicleModelExists(VehicleModel vehicleModel) {
        var errors = createErrorMap();

        if (announcementRepository.existsByVehicleModel(vehicleModel))
            errors.put("announcements", ValidationDetails.of(ValidatorCode.HAVE_REF_OBJECTS).appendDetail(vehicleModel.getId().toString()));

        return errors;
    }
}
