package com.app.validator;

import com.app.entity.Manufacturer;
import com.app.entity.VehicleModel;
import com.app.enums.ValidatorCode;
import com.app.repository.ManufacturerRepository;
import com.app.utils.Result;
import com.app.utils.ValidationDetails;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ManufacturerValidator implements ValidatorCommonMethods<Manufacturer> {

    private final ManufacturerRepository manufacturerRepository;
    private final VehicleModelValidator vehicleModelValidator;

    public ManufacturerValidator(ManufacturerRepository manufacturerRepository, VehicleModelValidator vehicleModelValidator) {
        this.manufacturerRepository = manufacturerRepository;
        this.vehicleModelValidator = vehicleModelValidator;
    }

    public Result checkBeforeSave(Manufacturer manufacturer) {
        Map<String, ValidationDetails> errors = new HashMap<>();

        errors.putAll(checkNameIsSet(manufacturer.getName()));
        errors.putAll(checkManufacturerWithSameNameExists(manufacturer));

        Result result = Result.create(errors);

        for (VehicleModel vehicleModel : manufacturer.getVehicleModelAsOptional().orElse(Collections.emptyList()))
            result.appendResult(vehicleModelValidator.checkBeforeSave(vehicleModel));

        return Result.create(errors);
    }

    private Map<String, ValidationDetails> checkNameIsSet(String name) {
        var errors = createErrorMap();

        if (StringUtils.isBlank(name))
            errors.put("name", ValidationDetails.of(ValidatorCode.IS_EMPTY));

        return errors;
    }

    private Map<String, ValidationDetails> checkManufacturerWithSameNameExists(Manufacturer manufacturer) {
        var errors = createErrorMap();

        List<Manufacturer> manufacturersWithSameName = manufacturerRepository.findByName(manufacturer.getName());

        if (manufacturer.getId() == null && manufacturersWithSameName.stream().anyMatch(m -> m.getName().equals(manufacturer.getName()))) {
            errors.put("name", ValidationDetails.of(ValidatorCode.ALREADY_EXISTS));
        } else if (manufacturer.getId() != null && manufacturersWithSameName.stream().anyMatch(m -> !manufacturer.getId().equals(m.getId()) && m.getName().equals(manufacturer.getName()))) {
            errors.put("name", ValidationDetails.of(ValidatorCode.ALREADY_EXISTS));
        }

        return errors;
	}

	public Result checkBeforeDelete(Manufacturer manufacturer) {
        Result result = Result.success();

        for (VehicleModel vehicleModel : manufacturer.getVehicleModelAsOptional().orElse(Collections.emptyList()))
            result.appendResult(vehicleModelValidator.checkBeforeDelete(vehicleModel));

        return result;
    }

}
