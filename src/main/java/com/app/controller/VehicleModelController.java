package com.app.controller;

import com.app.announcement.types.VehicleType;
import com.app.vehiclemodel.entity.VehicleModel;
import com.app.vehiclemodel.repository.VehicleModelRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("vehicleModel/")
public class VehicleModelController {

    private final VehicleModelRepository vehicleModelRepository;

    public VehicleModelController(VehicleModelRepository vehicleModelRepository) {
        this.vehicleModelRepository = vehicleModelRepository;
    }

    @RequestMapping(value = "loadVehicleModel", method = RequestMethod.GET)
    public @ResponseBody
    String loadVehicleModel(@RequestParam("manufacturer") String manufacturer,
                            @RequestParam("vehicleType") VehicleType vehicleType,
                            @RequestParam(value = "typeOfHtmlElement", defaultValue = "li") String htmlElement) {

        List<VehicleModel> vehicleModels =
                vehicleModelRepository.findByManufacturerIdAndVehicleType(Long.valueOf(manufacturer), vehicleType);

        return vehicleModels.stream()
                .map(e -> "<" + htmlElement + " value='" + e.getId() + "'>" + e.getName() + "</" + htmlElement + ">")
                .collect(Collectors.joining(""));
    }
}
