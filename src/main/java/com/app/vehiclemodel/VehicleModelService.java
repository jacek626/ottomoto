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

    public Result<VehicleModel> save(VehicleModel vehicleModel) {
        return vehicleModelValidator.validateForSave(vehicleModel).
                ifSuccess(() -> vehicleModelRepository.save(vehicleModel));
    }

    public Result<VehicleModel> delete(VehicleModel vehicleModel) {
        return vehicleModelValidator.validateForDelete(vehicleModel)
                .ifSuccess(() -> vehicleModelRepository.delete(vehicleModel));
    }
}
