package com.app.controller;

import com.app.entity.Manufacturer;
import com.app.entity.QManufacturer;
import com.app.entity.VehicleModel;
import com.app.enums.VehicleSubtype;
import com.app.enums.VehicleType;
import com.app.repository.ManufacturerRepository;
import com.app.repository.VehicleRepository;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("manufacturer/")
public class ManufacturerController {
	
	@Autowired
	ManufacturerRepository manufacturerRepository;
	
	@Autowired
	VehicleRepository vehicleRepository;
	
	private final int[] PAGE_SIZES = {5,10,20};
	
	 @ModelAttribute
	    public void addAttributes(Model model) {
			model.addAttribute("vehicleSubtypesCar", VehicleSubtype.vehicleSubtypesWithLabels(VehicleType.CAR));
			model.addAttribute("vehicleSubtypesTruck", VehicleSubtype.vehicleSubtypesWithLabels(VehicleType.TRUCK));
			model.addAttribute("vehicleSubtypesMotorcycle", VehicleSubtype.vehicleSubtypesWithLabels(VehicleType.MOTORCYCLE));
	    }
	
	@RequestMapping(value="new",method=RequestMethod.GET)
	public String register(Model model) {
		model.addAttribute("manufacturer", new Manufacturer());
	//	model.addAttribute("vehicleTypes", VehicleType.vehicleTypesWithLabels());
		
		return "manufacturer/manufacturerEdit";
	}
	
	
	@RequestMapping(value="save",method=RequestMethod.POST, params="action=addVehicle")
	public String addVehicle(@ModelAttribute("manufacturer") Manufacturer manufacturer,Model model,
			Authentication authentication, RedirectAttributes redirectAttributes) {
		
	//	model.addAttribute("vehicleTypes", VehicleType.vehicleTypesWithLabels());
		
		VehicleModel vehicle =new VehicleModel();
		manufacturer.getVehicleModel().add(vehicle);
		model.addAttribute("manufacturer",manufacturer);
		
		// redirectAttributes.addFlashAttribute
		redirectAttributes.addAttribute("manufacturer", manufacturer);
		redirectAttributes.addFlashAttribute("manufacturerWithAddedModel", manufacturer);
		redirectAttributes.addFlashAttribute("manufacturer", manufacturer);
			
		return "redirect:/manufacturer/edit/" + manufacturer.getId();
	}
	
	@RequestMapping(value="save",method=RequestMethod.POST, params="action=removeVehicle")
	public String removeVehicle(@ModelAttribute("manufacturer") Manufacturer manufacturer,Model model,
			@RequestParam("vehicleToDelete") Long vehicleId, Authentication authentication) {
		
	//	model.addAttribute("vehicleTypes", VehicleType.vehicleTypesWithLabels());
		
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
		
		if(model.asMap().containsKey("manufacturerWithAddedModel"))
			model.addAttribute("manufacturer",model.asMap().get("manufacturerWithAddedModel"));
		else
			model.addAttribute("manufacturer",manufacturer);
		
	//	model.addAttribute("vehicleTypes", VehicleType.vehicleTypesWithLabels());
		
		return "manufacturer/manufacturerEdit";
	}
	
	@RequestMapping(value="delete/{id}",method=RequestMethod.GET)
	public String delete(@PathVariable("id") Long id, Model model) {
		
		manufacturerRepository.deleteById(id);
		
		return "redirect:/mark/list";
	}
	
	@RequestMapping(value="/list")
	public String list(@RequestParam(name = "page", required = false) Optional<Integer> page,
			@RequestParam(name = "size", required = false, defaultValue = "10") Optional<Integer> size,
			@RequestParam(name = "orderBy", required = false, defaultValue = "id") Optional<String> orderBy,
			@RequestParam(name = "sort", required = false, defaultValue = "ASC") Optional<String> sort,
			@RequestParam(name = "searchArguments",required = false, defaultValue = "&") String searchArguments, 
			@ModelAttribute("manufacturer")  Manufacturer manufacturer, 
			Model model)  {
		
		manufacturer.prepareFiledsForSearch();
		
		BooleanBuilder queryBuilder = new BooleanBuilder();
		searchArguments = prepareQueryAndSearchArguments(manufacturer, searchArguments, queryBuilder);
		
		PageRequest pageable = null;
		pageable = PageRequest.of(page.orElse(1) - 1, size.orElse(10), Direction.fromString(sort.orElse("ASC")), orderBy.orElse("id"));
	
		Page<Manufacturer> manufacturerPage = manufacturerRepository.findAll(queryBuilder, pageable);
		
	    if(manufacturerPage.getTotalPages() > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1 ,manufacturerPage.getTotalPages()).boxed().collect(Collectors.toList());
            model.addAttribute("numbers", pageNumbers);
        }
        
        model.addAttribute("pages", manufacturerPage);
        model.addAttribute("orderBy", orderBy.orElse(""));
        model.addAttribute("sort", sort.orElse("ASC"));
        model.addAttribute("searchArguments", searchArguments);
        model.addAttribute("page", page.orElse(1));
        model.addAttribute("size", size.orElse(10));
        model.addAttribute("pageSizes", PAGE_SIZES);
        model.addAttribute("manufacturer", manufacturer);
		
		return "/manufacturer/manufacturerList";
	}
	
	private String prepareQueryAndSearchArguments(Manufacturer manufacturer, String searchArguments, BooleanBuilder queryBuilder) {
		StringBuilder stringBuilder = new StringBuilder(searchArguments);
		if(StringUtils.isNotBlank(manufacturer.getName())) {
			queryBuilder.and(QManufacturer.manufacturer.name.contains(manufacturer.getName()));
			stringBuilder.append("name=" + manufacturer.getName() + "&");
		}
		
		if(StringUtils.isNotBlank(manufacturer.getVehicleModelName())) {
			queryBuilder.and(QManufacturer.manufacturer.vehicleModel.any().name.contains(manufacturer.getVehicleModelName()));
			stringBuilder.append("vehicleModelName=" + manufacturer.getVehicleModelName() + "&");
		}
		
		return stringBuilder.toString();
	}
	
	
	@RequestMapping(value="loadVehicleSubtypes",method=RequestMethod.GET)
	public @ResponseBody String loadVehicleSubtypes(@RequestParam("vehicleType") String vehicleType,
			@RequestParam(value="typeOfHtmlElement", defaultValue = "li") String htmlElement, Model model,
			Authentication authentication, ModelMap map) {
	
	//	Map<VehicleSubtype,String> vehicleSubtypeMap = VehicleSubtype.vehicleSubtypesWithLabels(VehicleType.valueOf(vehicleType));
		List<VehicleSubtype> vehicleSubtypes = VehicleSubtype.vehicleSubtypesWithLabels(VehicleType.valueOf(vehicleType));

/*		for (Map.Entry<VehicleSubtype, String> entry: vehicleSubtypeMap.entrySet()) {
			strBuilder.append("<option value='" + entry.getKey() + "'>" + entry.getValue() + "</option>");
		}*/
		
		String fromStream =  vehicleSubtypes.stream()
				.map(e ->  "<" + htmlElement + " value='" + e.getVehicleType() + "'>" + e.getLabel() + "</"+ htmlElement +">")
				.collect(Collectors.joining("")).toString();
		
/*		return (resultWithEmptyOption ? "<option value=''></option>" : "")  + fromStream;*/
		return fromStream;
	}
	
	

}
