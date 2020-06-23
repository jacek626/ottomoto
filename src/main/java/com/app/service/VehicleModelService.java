package com.app.service;

import com.app.entity.VehicleModel;
import com.app.repository.VehicleModelRepository;
import com.app.utils.Result;
import com.app.validator.VehicleModelValidator;
import org.springframework.stereotype.Service;

@Service
public class VehicleModelService {

    private final VehicleModelRepository vehicleModelRepository;

    private final VehicleModelValidator vehicleModelValidator;

    public VehicleModelService(VehicleModelRepository vehicleModelRepository, VehicleModelValidator vehicleModelValidator) {
        this.vehicleModelRepository = vehicleModelRepository;
        this.vehicleModelValidator = vehicleModelValidator;
    }

    public Result save(VehicleModel vehicleModel) {
        Result<VehicleModel> result = vehicleModelValidator.checkBeforeSave(vehicleModel);

        if (result.isSuccess())
            vehicleModelRepository.save(vehicleModel);

        return result;
    }

    public Result delete(VehicleModel vehicleModel) {
        Result result = vehicleModelValidator.checkBeforeDelete(vehicleModel);

        if (result.isSuccess())
            vehicleModelRepository.delete(vehicleModel);

        return result;
    }
}
