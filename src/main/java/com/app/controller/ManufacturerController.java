package com.app.controller;

import com.app.entity.Manufacturer;
import com.app.entity.QManufacturer;
import com.app.entity.VehicleModel;
import com.app.enums.VehicleSubtype;
import com.app.enums.VehicleType;
import com.app.projection.ManufacturerProjection;
import com.app.repository.ManufacturerRepository;
import com.app.repository.VehicleRepository;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.lang3.StringUtils;
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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("manufacturer/")
public class ManufacturerController {
	
	private final ManufacturerRepository manufacturerRepository;
	
	private final VehicleRepository vehicleRepository;
	
	private final int[] PAGE_SIZES = {5,10,20};

	public ManufacturerController(ManufacturerRepository manufacturerRepository, VehicleRepository vehicleRepository) {
		this.manufacturerRepository = manufacturerRepository;
		this.vehicleRepository = vehicleRepository;
	}

	@ModelAttribute
	    public void addAttributes(Model model) {
			model.addAttribute("vehicleSubtypesCar", VehicleSubtype.getVehicleSubtypesByVehicleType(VehicleType.CAR));
			model.addAttribute("vehicleSubtypesTruck", VehicleSubtype.getVehicleSubtypesByVehicleType(VehicleType.TRUCK));
			model.addAttribute("vehicleSubtypesMotorcycle", VehicleSubtype.getVehicleSubtypesByVehicleType(VehicleType.MOTORCYCLE));
	    }
	
	@RequestMapping(value="new",method=RequestMethod.GET)
	public String register(Model model) {
		model.addAttribute("manufacturer", new Manufacturer());

		return "manufacturer/manufacturerEdit";
	}
	
	
	@RequestMapping(value="save",method=RequestMethod.POST, params="action=addVehicle")
	public String addVehicle(@ModelAttribute("manufacturer") Manufacturer manufacturer,Model model,
			Authentication authentication, RedirectAttributes redirectAttributes) {
		
		VehicleModel vehicle =new VehicleModel();
		manufacturer.getVehicleModel().add(vehicle);
		model.addAttribute("manufacturer",manufacturer);
		
		redirectAttributes.addAttribute("manufacturer", manufacturer);
		redirectAttributes.addFlashAttribute("manufacturerWithAddedModel", manufacturer);
		redirectAttributes.addFlashAttribute("manufacturer", manufacturer);
			
		return "redirect:/manufacturer/edit/" + manufacturer.getId();
	}
	
	@RequestMapping(value="save",method=RequestMethod.POST, params="action=removeVehicle")
	public String removeVehicle(@ModelAttribute("manufacturer") Manufacturer manufacturer,Model model,
			@RequestParam("vehicleToDelete") Long vehicleId, Authentication authentication) {

		manufacturer.getVehicleModel().remove(manufacturer.getVehicleModel().stream().filter(v -> v.getToDelete() == true).findFirst().get());
		model.addAttribute("manufacturer",manufacturer);

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
		

		return "manufacturer/manufacturerEdit";
	}
	
	@RequestMapping(value="delete/{id}",method=RequestMethod.GET)
	public String delete(@PathVariable("id") Long id, Model model) {
		
		manufacturerRepository.deleteById(id);
		
		return "redirect:/mark/list";
	}
	
	@RequestMapping(value="/list")
	public String list(@RequestParam(name = "page", defaultValue = "1", required = false) int page,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "orderBy", required = false, defaultValue = "id") String orderBy,
			@RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort,
			@RequestParam(name = "searchArguments",required = false, defaultValue = "&") String searchArguments, 
			@ModelAttribute("manufacturer")  Manufacturer manufacturer, 
			Model model)  {
		
		manufacturer.prepareFiledsForSearch();
		
		BooleanBuilder queryBuilder = new BooleanBuilder();
		searchArguments = prepareQueryAndSearchArguments(manufacturer, searchArguments, queryBuilder);
		
		PageRequest pageable = null;
		pageable = PageRequest.of(page, size, Direction.fromString(sort), orderBy);
	
		Page<Manufacturer> manufacturerPage = manufacturerRepository.findAll(queryBuilder, pageable);
		
	    if(manufacturerPage.getTotalPages() > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1 ,manufacturerPage.getTotalPages()).boxed().collect(Collectors.toList());
            model.addAttribute("numbers", pageNumbers);
        }
        
        model.addAttribute("pages", manufacturerPage);
        model.addAttribute("orderBy", orderBy);
        model.addAttribute("sort", sort);
        model.addAttribute("searchArguments", searchArguments);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("pageSizes", PAGE_SIZES);
        model.addAttribute("manufacturer", manufacturer);
		
		return "/manufacturer/manufacturerList";
	}
	
	private String prepareQueryAndSearchArguments(Manufacturer manufacturer, String searchArguments, BooleanBuilder queryBuilder) {
		StringBuilder stringBuilder = new StringBuilder(searchArguments);
		if(StringUtils.isNotBlank(manufacturer.getName())) {
			queryBuilder.and(QManufacturer.manufacturer.name.contains(manufacturer.getName()));
			stringBuilder.append("name=").append(manufacturer.getName()).append("&");
		}
		
		if(StringUtils.isNotBlank(manufacturer.getVehicleModelName())) {
			queryBuilder.and(QManufacturer.manufacturer.vehicleModel.any().name.contains(manufacturer.getVehicleModelName()));
			stringBuilder.append("vehicleModelName=").append(manufacturer.getVehicleModelName()).append("&");
		}
		
		return stringBuilder.toString();
	}
	
	
	@RequestMapping(value="loadVehicleSubtypes",method=RequestMethod.GET)
	public @ResponseBody String loadVehicleSubtypes(@RequestParam("vehicleType") String vehicleType,
			@RequestParam(value="typeOfHtmlElement", defaultValue = "li") String htmlElement, Model model,
			Authentication authentication, ModelMap map) {
	
		List<VehicleSubtype> vehicleSubtypes = VehicleSubtype.getVehicleSubtypesByVehicleType(VehicleType.valueOf(vehicleType));


		String fromStream =  vehicleSubtypes.stream()
				.map(e ->  "<" + htmlElement + " value='" + e.getVehicleType() + "'>" + e.getLabel() + "</"+ htmlElement +">")
				.collect(Collectors.joining(""));
		
		return fromStream;
	}


	@RequestMapping(value="loadManufacturer",method=RequestMethod.GET)
	public @ResponseBody String loadManufacturer(
			@RequestParam("vehicleType") String vehicleType,
			@RequestParam("typeOfHtmlElement") String htmlElement, Model model,
			Authentication authentication, ModelMap map) {

		List<ManufacturerProjection> manufacturerList = manufacturerRepository.findByVehicleType(VehicleType.valueOf(vehicleType));


		String fromStream =  manufacturerList.stream()
				.map(e -> "<" + htmlElement + " value='" + e.getId() + "'>" + e.getName() + "</"+ htmlElement +">")
				.collect(Collectors.joining(""));

		return fromStream;
	}
	
	

}
