package com.app.controller;

import com.app.entity.Manufacturer;
import com.app.entity.VehicleModel;
import com.app.enums.VehicleSubtype;
import com.app.enums.VehicleType;
import com.app.projection.ManufacturerProjection;
import com.app.repository.ManufacturerRepository;
import com.app.searchform.SearchStrategy;
import com.app.service.ManufacturerService;
import com.app.service.VehicleModelService;
import com.app.utils.PaginationDetails;
import com.app.utils.Result;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("manufacturer/")
public class ManufacturerController {

    private final ManufacturerRepository manufacturerRepository;

    private final SearchStrategy manufacturerSearchStrategy;

    private final ManufacturerService manufacturerService;

    private final VehicleModelService vehicleModelService;


    public ManufacturerController(ManufacturerRepository manufacturerRepository, SearchStrategy manufacturerSearchStrategy, ManufacturerService manufacturerService, VehicleModelService vehicleModelService) {
        this.manufacturerRepository = manufacturerRepository;
        this.manufacturerSearchStrategy = manufacturerSearchStrategy;
        this.manufacturerService = manufacturerService;
        this.vehicleModelService = vehicleModelService;
    }

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("vehicleSubtypesCar", VehicleSubtype.getVehicleSubtypesByVehicleType(VehicleType.CAR));
        model.addAttribute("vehicleSubtypesTruck", VehicleSubtype.getVehicleSubtypesByVehicleType(VehicleType.TRUCK));
        model.addAttribute("vehicleSubtypesMotorcycle", VehicleSubtype.getVehicleSubtypesByVehicleType(VehicleType.MOTORCYCLE));
    }
	
/*
	@RequestMapping(value="new",method=RequestMethod.GET)
	public String register(Model model) {
		model.addAttribute("manufacturer", new Manufacturer());

		return "manufacturer/manufacturerEdit";
	}
*/


    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String create(Model model) {
        Manufacturer manufacturer = Manufacturer.builder().build();
        model.addAttribute("manufacturer", manufacturer);
        model.addAllAttributes(manufacturerSearchStrategy.prepareDataForHtmlElements(manufacturer));

        return "manufacturer/manufacturerEdit";
    }


    @RequestMapping(value = "save", method = RequestMethod.POST, params = "action=addVehicle")
    public String addVehicle(@ModelAttribute("manufacturer") Manufacturer manufacturer, Model model, RedirectAttributes redirectAttributes) {
        VehicleModel vehicleModel = new VehicleModel();
        vehicleModel.setManufacturer(manufacturer);
        manufacturer.getVehicleModelAsOptional().ifPresentOrElse(e -> {
            e.add(vehicleModel);
        }, () -> {
            manufacturer.setVehicleModel(Lists.newArrayList());
            manufacturer.getVehicleModel().add(vehicleModel);
        });
        model.addAttribute("manufacturer", manufacturer);

        //    redirectAttributes.addAttribute("manufacturer", manufacturer);
        redirectAttributes.addFlashAttribute("manufacturerWithAddedModel", manufacturer);
        //     redirectAttributes.addFlashAttribute("manufacturer", manufacturer);

        if (manufacturer.getId() == null)
            return "manufacturer/manufacturerEdit";

        return "redirect:/manufacturer/edit/" + manufacturer.getId();
    }

    @RequestMapping(value = "save", method = RequestMethod.POST, params = "action=removeVehicle")
    public String removeVehicle(@ModelAttribute("manufacturer") Manufacturer manufacturer, Model model, BindingResult bindingResult) {
        manufacturer.getVehicleModel().stream().filter(v -> v.getToDelete()).findFirst().ifPresent(vehicleModel -> {
            manufacturer.getVehicleModel().remove(vehicleModel);
 /*           Result<VehicleModel> result = vehicleModelService.delete(vehicleModel);
            result.ifSuccess(() -> {
                manufacturer.getVehicleModel().remove(vehicleModel);
            });
            result.ifError(() -> {
                result.convertToMvcError(bindingResult);
            });*/
        });

        return "manufacturer/manufacturerEdit";
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute("manufacturer") Manufacturer manufacturer, BindingResult bindingResult) {
        ModelAndView model = new ModelAndView("redirect:/manufacturer/list");

/*        for (VehicleModel vehicle : manufacturer.getVehicleModelAsOptional().orElse(Collections.emptyList()).stream().filter(v -> v.getManufacturer() == null).collect(Collectors.toList())) {
            vehicle.setManufacturer(manufacturer);
        }   */

        if (bindingResult.hasErrors())
            model.setViewName("manufacturer/manufacturerEdit");
        else {
            Result<Manufacturer> result = manufacturerService.saveManufacturer(manufacturer);
            result.ifError(() -> {
                result.convertToMvcError(bindingResult);
                model.setViewName("manufacturer/manufacturerEdit");
            });
        }

        return model;
    }

    @RequestMapping(value = "addVehicle")
    public String addVehicle() {


        return "manufacturer/edit/";
    }

    @RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") Long id, Model model) {

        manufacturerRepository.findById(id).ifPresentOrElse(manufacturer -> {
            model.addAttribute("manufacturer", model.asMap().getOrDefault("manufacturerWithAddedModel", manufacturer));
        }, () -> {
            throw new IllegalArgumentException("Manufacturer not found");
        });


        return "manufacturer/manufacturerEdit";
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable("id") Long id) {

        manufacturerRepository.deleteById(id);

        return "redirect:/mark/list";
    }

    @RequestMapping(value = "/list")
    public String list(@RequestParam(name = "page", defaultValue = "1", required = false) int page,
                       @RequestParam(name = "size", required = false, defaultValue = "10") int size,
                       @RequestParam(name = "orderBy", required = false, defaultValue = "id") String orderBy,
                       @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort,
                       @ModelAttribute("manufacturer") Manufacturer manufacturer,
                       Model model) {

        model.addAttribute("requestMapping", "list");
        PaginationDetails paginationDetails = PaginationDetails.builder().page(page).size(size).orderBy(orderBy).sort(sort).build();
        model.addAllAttributes(manufacturerSearchStrategy.prepareSearchForm(manufacturer, paginationDetails));

        return "manufacturer/manufacturerList";
    }

    @RequestMapping(value = "loadVehicleSubtypes", method = RequestMethod.GET)
    public @ResponseBody
    String loadVehicleSubtypes(
            @RequestParam("vehicleType") String vehicleType,
            @RequestParam(value = "typeOfHtmlElement") String htmlElement) {

        List<VehicleSubtype> vehicleSubtypes = VehicleSubtype.getVehicleSubtypesByVehicleType(VehicleType.valueOf(vehicleType));

        return vehicleSubtypes.stream()
                .map(e -> "<" + htmlElement + " value='" + e.getVehicleType() + "'>" + e.getLabel() + "</" + htmlElement + ">")
                .collect(Collectors.joining(""));
    }


	@RequestMapping(value = "loadManufacturer", method = RequestMethod.GET)
    public @ResponseBody
    String loadManufacturer(
            @RequestParam("vehicleType") String vehicleType,
            @RequestParam("typeOfHtmlElement") String htmlElement) {

        List<ManufacturerProjection> manufacturerList = manufacturerRepository.findByVehicleType(VehicleType.valueOf(vehicleType));

        return manufacturerList.stream()
                .map(e -> "<" + htmlElement + " value='" + e.getId() + "'>" + e.getName() + "</" + htmlElement + ">")
                .collect(Collectors.joining(""));
    }
}
