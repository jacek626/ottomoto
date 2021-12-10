package com.app.manufacturer.validator;

import com.app.common.types.ValidatorCode;
import com.app.common.utils.validation.Result;
import com.app.common.utils.validation.ValidationDetails;
import com.app.common.validator.Validation;
import com.app.manufacturer.entity.Manufacturer;
import com.app.manufacturer.repository.ManufacturerRepository;
import com.app.vehiclemodel.entity.VehicleModel;
import com.app.vehiclemodel.validator.VehicleModelValidator;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class ManufacturerValidator implements Validation<Manufacturer> {
    private final ManufacturerRepository manufacturerRepository;
    private final VehicleModelValidator vehicleModelValidator;

    public Result<Manufacturer> validateForSave(Manufacturer manufacturer) {
        Map<String, ValidationDetails> errors = new HashMap<>();

        errors.putAll(checkNameIsSet(manufacturer.getName()));
        errors.putAll(checkManufacturerWithSameNameExists(manufacturer));

        var result = Result.create(errors);

        for (VehicleModel vehicleModel : manufacturer.getVehicleModelAsOptional().orElse(Collections.emptyList()))
            result.appendResult(vehicleModelValidator.validateForSave(vehicleModel));

        return Result.create(errors).setValidatedObject(manufacturer);
    }

    public Result<Manufacturer> validateForDelete(Manufacturer manufacturer) {
        var result = Result.success();

        for (VehicleModel vehicleModel : manufacturer.getVehicleModelAsOptional().orElse(Collections.emptyList()))
            result.appendResult(vehicleModelValidator.validateForDelete(vehicleModel));

        return result.setValidatedObject(manufacturer);
    }

    private Map<String, ValidationDetails> checkNameIsSet(String name) {
        var errors = createErrorsContainer();

        if (StringUtils.isBlank(name))
            errors.put("name", ValidationDetails.of(ValidatorCode.IS_EMPTY));

        return errors;
    }

    private Map<String, ValidationDetails> checkManufacturerWithSameNameExists(Manufacturer manufacturer) {
        var errors = createErrorsContainer();

        List<Manufacturer> manufacturersWithSameName = manufacturerRepository.findByName(manufacturer.getName());

        if (manufacturer.getId() == null && manufacturersWithSameName.stream().anyMatch(m -> m.getName().equals(manufacturer.getName()))) {
            errors.put("name", ValidationDetails.of(ValidatorCode.ALREADY_EXISTS));
        } else if (manufacturer.getId() != null && manufacturersWithSameName.stream().anyMatch(m -> !manufacturer.getId().equals(m.getId()) && m.getName().equals(manufacturer.getName()))) {
            errors.put("name", ValidationDetails.of(ValidatorCode.ALREADY_EXISTS));
        }

        return errors;
	}
}
