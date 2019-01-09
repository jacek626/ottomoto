package com.otomoto.controller;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.otomoto.entity.Manufacturer;
import com.otomoto.entity.VehicleModel;
import com.otomoto.enums.VehicleSubtype;
import com.otomoto.enums.VehicleType;
import com.otomoto.repository.ManufacturerRepository;
import com.otomoto.repository.VehicleRepository;

@Controller
@RequestMapping("manufacturer/")
public class ManufacturerController {
	
	@Autowired
	ManufacturerRepository manufacturerRepository;
	
	@Autowired
	VehicleRepository vehicleRepository;
	
	 @ModelAttribute
	    public void addAttributes(Model model) {
			model.addAttribute("vehicleSubtypesCar", VehicleSubtype.vehicleSubtypesWithLabels(VehicleType.CAR));
			model.addAttribute("vehicleSubtypesTruck", VehicleSubtype.vehicleSubtypesWithLabels(VehicleType.TRUCK));
			model.addAttribute("vehicleSubtypesMotorcycle", VehicleSubtype.vehicleSubtypesWithLabels(VehicleType.MOTORCYCLE));
	    }
	
	@RequestMapping(value="new",method=RequestMethod.GET)
	public String register(Model model) {
		
		model.addAttribute("manufacturer", new Manufacturer());
		model.addAttribute("vehicleTypes", VehicleType.vehicleTypesWithLabels());
		
		return "manufacturer/manufacturerEdit";
	}
	
	
	
	@RequestMapping(value="save",method=RequestMethod.POST, params="action=addVehicle")
	public String addVehicle(@ModelAttribute("manufacturer") Manufacturer manufacturer,Model model,
			Authentication authentication, RedirectAttributes redirectAttributes) {
		
		
		model.addAttribute("vehicleTypes", VehicleType.vehicleTypesWithLabels());
		
		if(manufacturer.getVehicleModel() == null)
			manufacturer.setVehicleModel(new ArrayList<VehicleModel>());
		
		VehicleModel vehicle =new VehicleModel();
		manufacturer.getVehicleModel().add(vehicle);
		model.addAttribute("manufacturer",manufacturer);
		
	//	redirectAttributes.addFlashAttribute("mark","mark");
	//	redirectAttributes.addAttribute("mark","mark");
	//	return "redirect:/mark/edit/" + mark.getId();
			
		return "manufacturermanufacturerkEdit";
	}
	
	@RequestMapping(value="save",method=RequestMethod.POST, params="action=removeVehicle")
	public String removeVehicle(@ModelAttribute("manufacturer") Manufacturer manufacturer,Model model,
			@RequestParam("vehicleToDelete") Long vehicleId, Authentication authentication) {
		
		model.addAttribute("vehicleTypes", VehicleType.vehicleTypesWithLabels());
		
		manufacturer.getVehicleModel().remove(manufacturer.getVehicleModel().stream().filter(v -> v.getToDelete() == true).findFirst().get());
		model.addAttribute("manufacturer",manufacturer);
	//	vehicleRepository.deleteById(vehicleId);
		
		return "manufacturer/manufacturerEdit";
	}
	
	@RequestMapping(value="save",method=RequestMethod.POST)
	public String registerPost(@ModelAttribute("manufacturer") Manufacturer manufacturer, Model model,
			Authentication authentication) {
		
		    if(manufacturer.getVehicleModel() != null) 
				for (VehicleModel vehicle : manufacturer.getVehicleModel().stream().filter(v -> v.getManufacturer() == null).collect(Collectors.toList())) {
				 	vehicle.setManufacturer(manufacturer);
				}

		manufacturerRepository.save(manufacturer);
		
		return "redirect:/manufacturer/list";
	}
	
	@RequestMapping(value="addVehicle")
	public String addVehicle() {
		
		
		return "manufacturer/edit/";
	}
	
	@RequestMapping(value="edit/{id}",method=RequestMethod.GET)
	public String edit(@PathVariable("id") Long id, Model model) {
		
		Manufacturer manufacturer = manufacturerRepository.findById(id).get();
		
		model.addAttribute("manufacturer",manufacturer);
		model.addAttribute("vehicleTypes", VehicleType.vehicleTypesWithLabels());
		
		return "manufacturer/manufacturerEdit";
	}
	
	@RequestMapping(value="delete/{id}",method=RequestMethod.GET)
	public String delete(@PathVariable("id") Long id, Model model) {
		
		manufacturerRepository.deleteById(id);
		
		return "redirect:/mark/list";
	}
	
	@RequestMapping(value="/list")
	public String list(Model model) {
		model.addAttribute("manufacturerList", manufacturerRepository.findAll());
		
		return "/manufacturer/manufacturerList";
	}
	
	
	@RequestMapping(value="loadVehicleSubtypes",method=RequestMethod.GET)
	public @ResponseBody String loadVehicleSubtypes(@RequestParam("vehicleType") String vehicleType, Model model,
			Authentication authentication, ModelMap map) {
	
//	public String loadVehicleSubtypes() {
		Map<VehicleSubtype,String> vehicleSubtypeMap = VehicleSubtype.vehicleSubtypesWithLabels(VehicleType.valueOf(vehicleType));
		
		StringBuilder strBuilder = new StringBuilder();
		
		for (Map.Entry<VehicleSubtype, String> entry: vehicleSubtypeMap.entrySet()) {
			strBuilder.append("<option value='" + entry.getKey() + "'>" + entry.getValue() + "</option>");
		}
		
		
//		vehicleSubtypeMap.entrySet().stream().forEach(e -> e.getValue());
		String fromStream =  vehicleSubtypeMap.entrySet().stream()
		.map(e -> "<option value='" + e.getKey() + "'>" + e.getValue() + "</option>")
		.collect(Collectors.joining("")).toString();
		
	//	System.out.println(strBuilder.toString());
		System.out.println(fromStream);
		return fromStream;
		//return "mark/markEdit";
	}
	
	@RequestMapping(value="save2",method=RequestMethod.GET)
	public @ResponseBody String addVehicleAjax(@ModelAttribute("mark") Manufacturer manufacturer,Model model,
			Authentication authentication, ModelMap map) {
		
		
		
		
		return "ffffffffffffffff";
		//return "mark/markEdit";
	}
	
	

}
