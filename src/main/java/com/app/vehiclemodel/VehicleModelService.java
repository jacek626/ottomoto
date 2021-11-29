package com.app.vehiclemodel;

import com.app.common.utils.validation.Result;
import com.app.vehiclemodel.entity.VehicleModel;
import com.app.vehiclemodel.repository.VehicleModelRepository;
import com.app.vehiclemodel.validator.VehicleModelValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class VehicleModelService {

    private final VehicleModelRepository vehicleModelRepository;
    private final VehicleModelValidator vehicleModelValidator;

    public Result save(VehicleModel vehicleModel) {
        Result<VehicleModel> result = vehicleModelValidator.checkBeforeSave(vehicleModel);
        result.ifSuccess(() -> vehicleModelRepository.save(vehicleModel));

        return result;
    }

    public Result delete(VehicleModel vehicleModel) {
        Result result = vehicleModelValidator.checkBeforeDelete(vehicleModel);
        result.ifSuccess(() -> vehicleModelRepository.delete(vehicleModel));

        return result;
    }
}
