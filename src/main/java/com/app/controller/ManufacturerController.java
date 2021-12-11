package com.app.controller;

import com.app.announcement.types.VehicleSubtype;
import com.app.announcement.types.VehicleType;
import com.app.common.utils.mapper.ManufacturerMapper;
import com.app.common.utils.search.PaginationDetails;
import com.app.manufacturer.ManufacturerService;
import com.app.manufacturer.dto.ManufacturerDto;
import com.app.manufacturer.entity.Manufacturer;
import com.app.manufacturer.repository.ManufacturerProjection;
import com.app.manufacturer.repository.ManufacturerRepository;
import com.app.searchform.SearchStrategy;
import com.app.vehiclemodel.entity.VehicleModel;
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

    private final SearchStrategy<Manufacturer, ManufacturerDto> manufacturerSearchStrategy;

    private final ManufacturerService manufacturerService;

    private final ManufacturerMapper manufacturerMapper;


    public ManufacturerController(ManufacturerRepository manufacturerRepository, SearchStrategy<Manufacturer, ManufacturerDto> manufacturerSearchStrategy, ManufacturerService manufacturerService, ManufacturerMapper manufacturerMapper) {
        this.manufacturerRepository = manufacturerRepository;
        this.manufacturerSearchStrategy = manufacturerSearchStrategy;
        this.manufacturerService = manufacturerService;
        this.manufacturerMapper = manufacturerMapper;
    }

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("vehicleSubtypesCar", VehicleSubtype.getVehicleSubtypesByVehicleType(VehicleType.CAR));
        model.addAttribute("vehicleSubtypesTruck", VehicleSubtype.getVehicleSubtypesByVehicleType(VehicleType.TRUCK));
        model.addAttribute("vehicleSubtypesMotorcycle", VehicleSubtype.getVehicleSubtypesByVehicleType(VehicleType.MOTORCYCLE));
    }
	
    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String create(Model model) {
        var manufacturer = Manufacturer.builder().build();
        model.addAttribute("manufacturer", manufacturerMapper.convertToDto(manufacturer));
        model.addAllAttributes(manufacturerSearchStrategy.prepareDataForHtmlElements(manufacturer));

        return "manufacturer/manufacturerEdit";
    }

    @RequestMapping(value = "save", method = RequestMethod.POST, params = "action=addVehicle")
    public String addVehicle(@ModelAttribute("manufacturer") ManufacturerDto manufacturerDto, Model model, RedirectAttributes redirectAttributes) {
        var manufacturer = manufacturerService.addVehicle(manufacturerMapper.convertToEntity(manufacturerDto));

        if (manufacturer.getId() == null) {
            model.addAttribute("manufacturer", manufacturerMapper.convertToDto(manufacturer));
            return "manufacturer/manufacturerEdit";
        }

        redirectAttributes.addFlashAttribute("manufacturerWithAddedModel", manufacturerMapper.convertToDto(manufacturer));
        return "redirect:/manufacturer/edit/" + manufacturer.getId();
    }

    @RequestMapping(value = "save", method = RequestMethod.POST, params = "action=removeVehicle")
    public String removeVehicle(@ModelAttribute("manufacturer") Manufacturer manufacturer) {
        manufacturer.getVehicleModel().stream().filter(VehicleModel::getToDelete).findFirst()
                .ifPresent(vehicleModel -> manufacturer.getVehicleModel().remove(vehicleModel));

        return "manufacturer/manufacturerEdit";
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute("manufacturer") ManufacturerDto manufacturerDto, BindingResult bindingResult) {
        var model = new ModelAndView("redirect:/manufacturer/list");

        if (bindingResult.hasErrors())
            model.setViewName("manufacturer/manufacturerEdit");
        else {
            var result = manufacturerService.saveManufacturer(manufacturerMapper.convertToEntity(manufacturerDto));
            result.ifError(() -> {
                result.convertToMvcError(bindingResult);
                model.setViewName("manufacturer/manufacturerEdit");
            });
        }

        return model;
    }

    @RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") Long id, Model model) {

        manufacturerRepository.findById(id).ifPresentOrElse(manufacturer -> model.addAttribute("manufacturer", model.asMap().getOrDefault("manufacturerWithAddedModel", manufacturerMapper.convertToDto(manufacturer))), () -> {
            throw new IllegalArgumentException("Manufacturer not found");
        });


        return "manufacturer/manufacturerEdit";
    }

    @RequestMapping(value = "/list")
    public String list(@RequestParam(name = "page", defaultValue = "1", required = false) int page,
                       @RequestParam(name = "size", required = false, defaultValue = "10") int size,
                       @RequestParam(name = "orderBy", required = false, defaultValue = "id") String orderBy,
                       @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort,
                       @ModelAttribute("manufacturer") ManufacturerDto manufacturerDto,
                       Model model) {
        var paginationDetails = PaginationDetails.builder().page(page).size(size).orderBy(orderBy).sort(sort).build();
        model.addAllAttributes(manufacturerSearchStrategy.prepareSearchForm(manufacturerMapper.convertToEntity(manufacturerDto), paginationDetails));
        model.addAttribute("requestMapping", "list");

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
