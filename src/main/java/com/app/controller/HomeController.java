package com.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.app.entity.Announcement;
import com.app.enums.BooleanValuesForDropDown;
import com.app.enums.CarColor;
import com.app.enums.FuelType;
import com.app.enums.SearchEngineDropDownValues;
import com.app.enums.VehicleType;
import com.app.projection.ManufacturerProjection;
import com.app.repository.AnnouncementRepository;
import com.app.repository.ManufacturerRepository;

@Controller
public class HomeController {
	
	@Autowired
	private AnnouncementRepository announcementRepository;
	
	@Autowired
	private ManufacturerRepository manufacturerRepository;

    @RequestMapping("/")
    public String home(Model model, @ModelAttribute("announcement")  Announcement announcement) {
    	announcement.prepareFiledsForSearch();
    	
    	model.addAttribute("announcement", announcement);
    	model.addAttribute("newestAnnouncements", announcementRepository.findFirst20ByDeactivationDateIsNullOrderByCreationDateDesc());
    	
        model.addAttribute("pricesList", SearchEngineDropDownValues.CAR_PRICES_LIST);
        model.addAttribute("mileageList", SearchEngineDropDownValues.MILEAGE_LIST);
        model.addAttribute("engineCapacityList", SearchEngineDropDownValues.ENGINE_CAPACITY_LIST);
        model.addAttribute("enginePowerList", SearchEngineDropDownValues.ENGINE_POWER_LIST);
        model.addAttribute("doorsList", SearchEngineDropDownValues.DOOR_LIST);
        
        model.addAttribute("vehicleTypeList", VehicleType.vehicleTypesWithLabels());
        
        model.addAttribute("fuelTypeList", FuelType.values());
        model.addAttribute("colorList", CarColor.values());
        model.addAttribute("booleanValuesForDropDown", BooleanValuesForDropDown.values());
        
        List<ManufacturerProjection> manufacturerList = manufacturerRepository.findByVehicleType(announcement.getVehicleType());
		model.addAttribute("manufacturerList", manufacturerList);
    	
    	
        return "home";
    }
}
