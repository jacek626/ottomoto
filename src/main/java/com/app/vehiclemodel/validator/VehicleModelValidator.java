package com.app.vehiclemodel.validator;

import com.app.announcement.repository.AnnouncementRepository;
import com.app.common.types.ValidatorCode;
import com.app.common.utils.validation.Result;
import com.app.common.utils.validation.ValidationDetails;
import com.app.common.validator.Validation;
import com.app.vehiclemodel.entity.VehicleModel;
import com.app.vehiclemodel.repository.VehicleModelRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@AllArgsConstructor
public class VehicleModelValidator implements Validation<VehicleModel> {

    private final VehicleModelRepository vehicleModelRepository;
    private final AnnouncementRepository announcementRepository;

    @Override
    public Result<VehicleModel> validateForSave(VehicleModel vehicleModel) {
        var errors = createErrorsContainer();

        errors.putAll(checkNameIsNotEmpty(vehicleModel.getName()));
        errors.putAll(checkNameIsUnique(vehicleModel));

        return Result.create(errors).setValidatedObject(vehicleModel);
    }

    @Override
    public Result<VehicleModel> validateForDelete(VehicleModel vehicleModel) {
        var errors = createErrorsContainer();

        errors.putAll(checkIsAnnouncementsWithThisVehicleModelExists(vehicleModel));

        return Result.create(errors).setValidatedObject(vehicleModel);
    }

    private Map<String, ValidationDetails> checkNameIsUnique(VehicleModel vehicleModel) {
        var errors = createErrorsContainer();

        if (vehicleModel.getId() == null && vehicleModelRepository.countByName(vehicleModel.getName()) > 0) {
            errors.put("VehicleModelName", ValidationDetails.of(ValidatorCode.ALREADY_EXISTS, vehicleModel.getName()).appendDetail(vehicleModel.getName()).objectName("VehicleModel"));
        } else if (vehicleModel.getId() != null && vehicleModelRepository.countByNameAndIdNot(vehicleModel.getName(), vehicleModel.getId()) > 0) {
            errors.put("VehicleModelName", ValidationDetails.of(ValidatorCode.ALREADY_EXISTS, vehicleModel.getName()).appendDetail(vehicleModel.getName()).objectName("VehicleModel"));
        }

        return errors;
    }

    private Map<String, ValidationDetails> checkNameIsNotEmpty(String vehicleName) {
        var errors = createErrorsContainer();

        if (StringUtils.isBlank(vehicleName)) {
            errors.put("VehicleModelName", ValidationDetails.of(ValidatorCode.IS_EMPTY, "Nazwa modelu").objectName("VehicleModel"));
        }

        return errors;
    }

    private Map<String, ValidationDetails> checkIsAnnouncementsWithThisVehicleModelExists(VehicleModel vehicleModel) {
        var errors = createErrorsContainer();

        if (announcementRepository.existsByVehicleModel(vehicleModel))
            errors.put("announcements", ValidationDetails.of(ValidatorCode.HAVE_REF_OBJECTS).appendDetail(vehicleModel.getId().toString()));

        return errors;
    }
}
