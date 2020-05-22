package com.app.controller;

import com.app.entity.VehicleModel;
import com.app.enums.VehicleType;
import com.app.repository.VehicleModelRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
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

    @RequestMapping(value="loadVehicleModel",method= RequestMethod.GET)
    public @ResponseBody
    String loadVehicleModel(@RequestParam("manufacturer") String manufacturer,
                            @RequestParam("vehicleType") VehicleType vehicleType,
                            @RequestParam(value="typeOfHtmlElement", defaultValue = "li") String htmlElement, Model model,
                            Authentication authentication, ModelMap map) {


        if(StringUtils.isBlank(manufacturer))
            return "";

        List<VehicleModel> vehicleModelList =
                vehicleModelRepository.findByManufacturerIdAndVehicleType(Long.valueOf(manufacturer),vehicleType);


        return vehicleModelList.stream()
                .map(e -> "<" + htmlElement + " value='" + e.getId() + "'>" + e.getName() + "</" + htmlElement + ">")
                .collect(Collectors.joining(""));
    }
}
