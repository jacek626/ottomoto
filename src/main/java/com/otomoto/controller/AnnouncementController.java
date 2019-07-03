package com.otomoto.controller;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.validation.Valid;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.utils.HtmlElement;
import com.google.common.io.Files;
import com.otomoto.entity.Announcement;
import com.otomoto.entity.QAnnouncement;
import com.otomoto.entity.QManufacturer;
import com.otomoto.entity.VehicleModel;
import com.otomoto.enums.BooleanValuesForDropDown;
import com.otomoto.enums.CarColor;
import com.otomoto.enums.FuelType;
import com.otomoto.enums.SearchEngineDropDownValues;
import com.otomoto.enums.VehicleSubtype;
import com.otomoto.enums.VehicleType;
import com.otomoto.projection.ManufacturerProjection;
import com.otomoto.repository.AnnouncementRepository;
import com.otomoto.repository.ManufacturerRepository;
import com.otomoto.repository.UserRepository;
import com.otomoto.repository.VehicleModelRepository;
import com.querydsl.core.BooleanBuilder;

@Controller
@RequestMapping("announcement/")
public class AnnouncementController {
	
	@Autowired
	private AnnouncementRepository announcementRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ManufacturerRepository manufacturerRepository;
	
	@Autowired
	private VehicleModelRepository vehicleModelRepository;
	
	@Autowired
	private Environment environment;
	
	String repositoryLocation;
	
	private final int[] PAGE_SIZES = {5,10,20};
	
	@PostConstruct
	public void init() {
		repositoryLocation = environment.getProperty("spring.repository.location");
	}
	
	@RequestMapping(value="add",method=RequestMethod.GET)
	public String register(Model model) {
		VehicleType vehicleType = VehicleType.CAR;
		
		List<ManufacturerProjection> manufacturerList = manufacturerRepository.findByVehicleType(vehicleType);
		
		Announcement announcement = new Announcement();
		announcement.setFuelType(FuelType.PETROL);
		model.addAttribute("announcement", announcement);
		model.addAttribute("vehicleTypeList", VehicleType.vehicleTypesWithLabels());
		model.addAttribute("manufacturerList", manufacturerList);
		model.addAttribute("vehicleSubtypeList",VehicleSubtype.vehicleSubtypesWithLabels(vehicleType));
		model.addAttribute("colorList", CarColor.colorWithLabels());
		
		if(!manufacturerList.isEmpty())
			model.addAttribute("vehicleModelList", vehicleModelRepository.findByManufacturerId(manufacturerList.get(0).getId()));
		
		model.addAttribute("fuelTypeWithLabels", FuelType.fuelTypeWithLabels());
		
		return "announcement/announcementEdit";
	}
	
	@RequestMapping(value="edit/{id}",method=RequestMethod.GET)
	public String edit(@PathVariable("id") Long id, Model model) {
		Optional<Announcement> announcement = announcementRepository.findById(id);
		
		if(announcement.isPresent()) {
			VehicleType vehicleType = VehicleType.CAR;
			List<ManufacturerProjection> manufacturerList = manufacturerRepository.findByVehicleType(vehicleType);
			model.addAttribute("manufacturerList", manufacturerList);
			model.addAttribute("vehicleSubtypeList",VehicleSubtype.vehicleSubtypesWithLabels(vehicleType));
			model.addAttribute("vehicleTypeList", VehicleType.vehicleTypesWithLabels());
			model.addAttribute("colorList", CarColor.colorWithLabels());
			model.addAttribute("fuelTypeWithLabels", FuelType.fuelTypeWithLabels());
		
			if(model.asMap().containsKey("error")) {
				model.addAttribute("error", model.asMap().get("error"));
				model.addAttribute("announcement", model.asMap().get("announcement"));
			}
			else
				model.addAttribute("announcement", announcement.get());
				
			if(!manufacturerList.isEmpty())
				model.addAttribute("vehicleModelList", vehicleModelRepository.findByManufacturerId(announcement.get().getManufacturer().getId()));
		}
		
		return "announcement/announcementEdit";
	}
	
	
	private void imagesToDelete(List<String> imageNames) {
		String repositoryLocation = environment.getProperty("spring.repository.location");
		
		for (String name : imageNames) {
		 	 Paths.get(repositoryLocation, name).toFile().delete();
		}
	}
	
	
	@RequestMapping(value="save",method=RequestMethod.POST)
	public String save(@ModelAttribute("announcement") @Validated Announcement announcement,
			BindingResult bindingResult, Errors errors, Model model, Authentication authentication,
			RedirectAttributes redirectAttributes) {
		
		
		if(announcement.getPictures() != null) {
			imagesToDelete(announcement.getPictures().stream().filter(i -> i.isPictureToDelete()).map(p -> p.getRepositoryName()).collect(Collectors.toList()));
			announcement.setPictures(announcement.getPictures().stream().filter(i -> !i.isPictureToDelete()).collect(Collectors.toList()));
		}
		announcement.getPictures().stream().filter(p -> p.getAnnouncement() == null).forEach(p -> p.setAnnouncement(announcement));
		
		
		
		
		if(errors.hasErrors() ||  bindingResult.hasErrors()) {
			//	model.addAttribute("announcementFrom", announcement);
			//	model.addAttribute("error",errors);
			redirectAttributes.addFlashAttribute("announcement",announcement);
			redirectAttributes.addFlashAttribute("error",errors);
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.announcement", bindingResult);
			return "redirect:/announcement/edit/" + announcement.getId();
		}
		
		
		if(announcement.getId() == null) {
			announcement.setUser(userRepository.findByLogin(authentication.getName()));
			announcementRepository.save(announcement);
		}
		else {
			announcementRepository.save(announcement);
		}
		
		return "redirect:/announcement/list";
	}

	
	@RequestMapping(value="loadManufacturer",method=RequestMethod.GET)
	public @ResponseBody String loadManufacturer(
			@RequestParam("vehicleType") String vehicleType,
			@RequestParam("typeOfHtmlElement") String htmlElement, Model model,
			Authentication authentication, ModelMap map) {
	
		List<ManufacturerProjection> manufacturerList = manufacturerRepository.findByVehicleType(VehicleType.valueOf(vehicleType));
		
		//String fromStream = "<option value=''></option>";
		
/*		String fromStream =  manufacturerList.stream()
		.map(e -> "<option value='" + e.getId() + "'>" + e.getName() + "</option>")
		.collect(Collectors.joining("")).toString();*/
		
		String fromStream =  manufacturerList.stream()
				.map(e -> "<" + htmlElement + " value='" + e.getId() + "'>" + e.getName() + "</"+ htmlElement +">")
				.collect(Collectors.joining("")).toString();
		
		
	/*	return (resultWithEmptyOption ? "<option value=''></option>" : "")  + fromStream;*/
		return fromStream;
	}
	
	@RequestMapping(value="loadVehicleModel",method=RequestMethod.GET)
	public @ResponseBody String loadVehicleModel(@RequestParam("manufacturer") String manufacturer,
			@RequestParam("vehicleType") VehicleType vehicleType,
			@RequestParam(value="typeOfHtmlElement", defaultValue = "li") String htmlElement, Model model,
			Authentication authentication, ModelMap map) {
		
		
		if(StringUtils.isBlank(manufacturer))
			return "";
		
		List<VehicleModel> vehicleModelList = 
				vehicleModelRepository.findByManufacturerIdAndVehicleType(Long.valueOf(manufacturer),vehicleType);
		
		
		String fromStream =  vehicleModelList.stream()
				.map(e -> "<" + htmlElement + " value='" + e.getId() + "'>" + e.getName() + "</"+ htmlElement +">")
				.collect(Collectors.joining("")).toString();
		
		
		
		return fromStream;
//		return (resultWithEmptyOption ? "<option value=''></option>" : "")  + fromStream;
	}
	
	
	
	@RequestMapping(value="uploadImage", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<String>> uploadImage(@RequestParam("uploadfile") MultipartFile[] uploadfiles, Model model,
			Authentication authentication, ModelMap map) {
		 StringBuilder elementsToReturn;
		 List<String> resultList = new ArrayList<String>();
		/* <li index='"+ currentMaxElementId +"' picturetodelete='false'>"+ result +"</li>*/
		 try {
			 
			 for (MultipartFile uploadfile : uploadfiles) {
				 elementsToReturn = new StringBuilder();
			elementsToReturn.append("<li index='LIST_ID' picturetodelete='false'>");
			 String repositoryLocation = environment.getProperty("spring.repository.location");
			 String randomFileName = new Date().getTime() + RandomStringUtils.randomNumeric(10) + "." + Files.getFileExtension(uploadfile.getOriginalFilename());
			 String filepath = Paths.get(repositoryLocation, randomFileName).toString();
			     
			    
			    // Save the file locally
			    File originalImageFile = new File(filepath);
			    BufferedOutputStream stream =  new BufferedOutputStream(new FileOutputStream(originalImageFile));
			    stream.write(uploadfile.getBytes());
			    stream.close();
			    
			    BufferedImage originalImage = ImageIO.read(originalImageFile);
			    int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
			    
			    BufferedImage resizeImageJpg = resizeImage(originalImage, type);
			    String convertedImageToMiniaturePath = FilenameUtils.concat(repositoryLocation,  FilenameUtils.getBaseName(randomFileName) + "-small." + FilenameUtils.getExtension(randomFileName));
			    File convertedImageToMiniature = new File(convertedImageToMiniaturePath);
			    ImageIO.write(resizeImageJpg, FilenameUtils.getExtension(randomFileName), convertedImageToMiniature); 
			    		
			
			    elementsToReturn.append(new HtmlElement.Builder("img").
			    		id("pictures[LIST_ID].repositoryName").
			    		src("/images/" + convertedImageToMiniature.getName()).
			    		classStyle("miniatureImageInImageScroller").
			    		onclick("showImage(this,$('#photoContainerMiniImage'))").
			    		picture("/images/"+randomFileName).
			    		index("LIST_ID").
			    		build().returnHtml());
			    
			     elementsToReturn.append(new HtmlElement.Builder("input").
			    		 type("text").
			    		 id("pictures[LIST_ID].repositoryName").
			    		 name("pictures[LIST_ID].repositoryName").
			    		 classStyle("displayNone").
			    		 value(randomFileName).build().returnHtml());
			     
			     
			     elementsToReturn.append(new HtmlElement.Builder("input").
			    		 type("text").
			    		 id("pictures[LIST_ID].fileName").
			    		 name("pictures[LIST_ID].fileName").
			    		 classStyle("displayNone").
			    		 value(uploadfile.getOriginalFilename()).build().returnHtml());
			     
			     elementsToReturn.append(new HtmlElement.Builder("input").
			    		 type("text").
			    		 id("pictures[LIST_ID].miniatureRepositoryName").
			    		 name("pictures[LIST_ID].miniatureRepositoryName").
			    		 classStyle("displayNone").
			    		 value(convertedImageToMiniature.getName()).build().returnHtml());
			     
			     elementsToReturn.append(new HtmlElement.Builder("button").
			    		 type("button").
			    		 onclick("deletePictureInAnnouncement(this);").
			    		 html("usun").build().returnHtml());
		 
			      elementsToReturn.append("</li>");
			     
			     resultList.add(elementsToReturn.toString());
		 
			 }
		 
		 
		 
		 
		 }
			  catch (Exception e) {
				 e.printStackTrace();
			    System.out.println(e.getMessage());
			    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			  }
			  
		 return new ResponseEntity<List<String>>(resultList, HttpStatus.OK);
	//	return new ResponseEntity<>(elementsToReturn.toString(),HttpStatus.OK);
	}
	
	private static BufferedImage resizeImage(BufferedImage originalImage, int type) {
		Float height = 200f;
		Float procent =  (height / (float) originalImage.getHeight()) * 100f;
		Float width = originalImage.getWidth()  * (procent/100.0f);
		
		BufferedImage resizedImage = new BufferedImage(width.intValue(), height.intValue(), type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, width.intValue(), height.intValue(), null);
		g.dispose();

		return resizedImage;
	}
	
	
	@RequestMapping(value="/list")
	public String list(@RequestParam(name = "page", required = false) Optional<Integer> page,
			@RequestParam(name = "size", required = false, defaultValue = "10") Optional<Integer> size,
			@RequestParam(name = "orderBy", required = false, defaultValue = "id") Optional<String> orderBy,
			@RequestParam(name = "sort", required = false, defaultValue = "ASC") Optional<String> sort,
			@RequestParam(name = "searchArguments",required = false, defaultValue = "&") String searchArguments, 
			@ModelAttribute("announcement")  Announcement announcement,
			Model model) {
		
		announcement.prepareFiledsForSearch();
		
		BooleanBuilder queryBuilder = new BooleanBuilder();
		searchArguments = prepareQueryAndSearchArguments(announcement, searchArguments, queryBuilder);
		
		PageRequest pageable = null;
		pageable = PageRequest.of(page.orElse(1) - 1, size.orElse(10), Direction.fromString(sort.orElse("ASC")), orderBy.orElse("id"));
	
		Page<Announcement> announcementPage = announcementRepository.findAll(queryBuilder, pageable);
		
        if(announcementPage.getTotalPages() > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1 ,announcementPage.getTotalPages()).boxed().collect(Collectors.toList());
            model.addAttribute("numbers", pageNumbers);
        }
        
        model.addAttribute("vehicleTypeList", VehicleType.vehicleTypesWithLabels());
       
		List<ManufacturerProjection> manufacturerList = manufacturerRepository.findByVehicleType(announcement.getVehicleType());
	//	model.addAttribute("manufacturerList", manufacturerList);
        
		List<ManufacturerProjection> manufacturerList2 = new ArrayList<ManufacturerProjection>(manufacturerList); 
		manufacturerList2.addAll(manufacturerRepository.findByVehicleType(announcement.getVehicleType()));
		manufacturerList2.addAll(manufacturerRepository.findByVehicleType(announcement.getVehicleType()));
		manufacturerList2.addAll(manufacturerRepository.findByVehicleType(announcement.getVehicleType()));
		manufacturerList2.addAll(manufacturerRepository.findByVehicleType(announcement.getVehicleType()));
		model.addAttribute("manufacturerList", manufacturerList2);
        
        if(announcement.getManufacturer() != null)
			model.addAttribute("vehicleModelList", vehicleModelRepository.findByManufacturerIdAndVehicleType(announcement.getManufacturer().getId(), announcement.getVehicleType()));
        
     //   model.addAttribute("vehicleSubtypeList",VehicleSubtype.vehicleSubtypesWithLabels(announcement.getVehicleType()));
        model.addAttribute("vehicleSubtypeList",VehicleSubtype.vehicleSubtypesWithLabels2(announcement.getVehicleType()));
        // vehicleModelRepository.findByManufacturerIdAndVehicleType(Long.valueOf(manufacturer),vehicleType);
        
        
        model.addAttribute("fuelTypeList", FuelType.values());
        model.addAttribute("pages", announcementPage);
        model.addAttribute("orderBy", orderBy.orElse(""));
        model.addAttribute("sort", sort.orElse("ASC"));
        model.addAttribute("searchArguments", searchArguments);
        model.addAttribute("page", page.orElse(1));
        model.addAttribute("size", size.orElse(10));
        model.addAttribute("pageSizes", PAGE_SIZES);
        model.addAttribute("announcement", announcement);
        model.addAttribute("colorList", CarColor.values());
        model.addAttribute("colorList", BooleanValuesForDropDown.values());
        
        model.addAttribute("pricesList", SearchEngineDropDownValues.CAR_PRICES_LIST);
        model.addAttribute("mileageList", SearchEngineDropDownValues.MILEAGE_LIST);
        model.addAttribute("engineCapacityList", SearchEngineDropDownValues.ENGINE_CAPACITY_LIST);
        model.addAttribute("enginePowerList", SearchEngineDropDownValues.ENGINE_POWER_LIST);
        model.addAttribute("doorsList", SearchEngineDropDownValues.DOOR_LIST);
        
		return "/announcement/announcementList";
	}
	
	private String prepareQueryAndSearchArguments(Announcement announcement, String searchArguments, BooleanBuilder queryBuilder) {
		StringBuilder stringBuilder = new StringBuilder(searchArguments);
		if(StringUtils.isNotBlank(announcement.getSearchFields().getPriceFrom())) {
			BigDecimal priceFrom = new BigDecimal(announcement.getSearchFields().getPriceFromWithoutSpaces());
			queryBuilder.and(QAnnouncement.announcement.price.goe(priceFrom));
			stringBuilder.append("priceFrom=" + priceFrom + "&");
		}
		if(StringUtils.isNotBlank(announcement.getSearchFields().getPriceTo())) { 
			BigDecimal priceTo = new BigDecimal(announcement.getSearchFields().getPriceToWithoutSpaces());
			queryBuilder.and(QAnnouncement.announcement.price.loe(priceTo));
			stringBuilder.append("priceTo=" + priceTo + "&");
		}
		if(announcement.getManufacturer() != null) {
			queryBuilder.and(QAnnouncement.announcement.manufacturer.name.contains(announcement.getManufacturer().getName()));
			stringBuilder.append("manufacturer=" + announcement.getManufacturer().getId() + "&");
		}
		
		if(announcement.getVehicleModel() != null) {
			queryBuilder.and(QAnnouncement.announcement.manufacturer.id.eq(announcement.getVehicleModel().getId()));
			stringBuilder.append("vehicleModel=" + announcement.getVehicleModel().getName() + "&");
		}
		
		if(announcement.getFuelType() != null) {
			queryBuilder.and(QAnnouncement.announcement.fuelType.eq(announcement.getFuelType()));
			stringBuilder.append("fuelType=" + announcement.getFuelType() + "&");
		}
		
		if(announcement.getVehicleSubtype() != null) {
			queryBuilder.and(QAnnouncement.announcement.vehicleSubtype.eq(announcement.getVehicleSubtype()));
			stringBuilder.append("vehicleSubtype=" + announcement.getVehicleSubtype() + "&");
		}
		
		if(announcement.getFirstOwner() != null) {
			queryBuilder.and(QAnnouncement.announcement.firstOwner.eq(announcement.getFirstOwner()));
			stringBuilder.append("firstOwner=" + announcement.getFirstOwner() + "&");
		}
		
		if(announcement.getAccidents() != null) {
		queryBuilder.and(QAnnouncement.announcement.firstOwner.eq(announcement.getAccidents()));
		stringBuilder.append("accidents=" + announcement.getAccidents() + "&");
		}
		
		if(announcement.getDamaged() != null) {
		queryBuilder.and(QAnnouncement.announcement.firstOwner.eq(announcement.getDamaged()));
		stringBuilder.append("damaged=" + announcement.getDamaged() + "&");
		}
		
		if(announcement.getNetPrice() != null) {
		queryBuilder.and(QAnnouncement.announcement.firstOwner.eq(announcement.getNetPrice()));
		stringBuilder.append("netPrice=" + announcement.getNetPrice() + "&");
		}
		
		if(announcement.getPriceNegotiate() != null) {
		queryBuilder.and(QAnnouncement.announcement.firstOwner.eq(announcement.getPriceNegotiate()));
		stringBuilder.append("priceNegotiate=" + announcement.getPriceNegotiate() + "&");
		}
		
	/*	if(announcement.getSearchFields().getProductionYearFromAsOpt().isPresent() && announcement.getSearchFields().getProductionYearToAsOpt().isPresent()) {
			queryBuilder.and(QAnnouncement.announcement.productionYear.between(
					announcement.getSearchFields().getProductionYearFrom(), 
					announcement.getSearchFields().getProductionYearTo()));
			stringBuilder.append("productionYearFrom=" + announcement.getSearchFields().getProductionYearFrom() + "&");
			stringBuilder.append("productionYearTo=" + announcement.getSearchFields().getProductionYearTo() + "&");
		}*/
		if(announcement.getSearchFields().getProductionYearFromAsOpt().isPresent()) {
			queryBuilder.and(QAnnouncement.announcement.productionYear.goe(announcement.getSearchFields().getProductionYearFrom()));
			stringBuilder.append("productionYearFrom=" + announcement.getSearchFields().getProductionYearFrom() + "&");
		}
		if(announcement.getSearchFields().getProductionYearToAsOpt().isPresent()) {
			queryBuilder.and(QAnnouncement.announcement.productionYear.lt(announcement.getSearchFields().getProductionYearTo()));
			stringBuilder.append("productionYearTo=" + announcement.getSearchFields().getProductionYearTo() + "&");
		}
		
		if(StringUtils.isNotBlank(announcement.getSearchFields().getMileageFrom())) {
			queryBuilder.and(QAnnouncement.announcement.mileage.goe(announcement.getSearchFields().getMileageFromAsInteger()));
		//	stringBuilder.append("productionYearFrom=" + announcement.getSearchFields().getProductionYearFrom() + "&");
		}
		if(StringUtils.isNotBlank(announcement.getSearchFields().getMileageTo())) {
			queryBuilder.and(QAnnouncement.announcement.mileage.lt(announcement.getSearchFields().getMileageToAsInteger()));
		//	stringBuilder.append("productionYearTo=" + announcement.getSearchFields().getProductionYearTo() + "&");
		}
		
		if(StringUtils.isNotBlank(announcement.getSearchFields().getEngineCapacityFrom())) {
			queryBuilder.and(QAnnouncement.announcement.engineCapacity.goe(announcement.getSearchFields().getEngineCapacityFromAsInteger()));
			//	stringBuilder.append("productionYearFrom=" + announcement.getSearchFields().getProductionYearFrom() + "&");
		}
		if(StringUtils.isNotBlank(announcement.getSearchFields().getEngineCapacityTo())) {
			queryBuilder.and(QAnnouncement.announcement.engineCapacity.lt(announcement.getSearchFields().getEngineCapacityToAsInteger()));
			//	stringBuilder.append("productionYearTo=" + announcement.getSearchFields().getProductionYearTo() + "&");
		}
		
		
		if(StringUtils.isNotBlank(announcement.getSearchFields().getEnginePowerFrom())) {
			queryBuilder.and(QAnnouncement.announcement.enginePower.goe(announcement.getSearchFields().getEnginePowerFromAsInteger()));
			//	stringBuilder.append("productionYearFrom=" + announcement.getSearchFields().getProductionYearFrom() + "&");
		}
		if(StringUtils.isNotBlank(announcement.getSearchFields().getEnginePowerTo())) {
			queryBuilder.and(QAnnouncement.announcement.enginePower.lt(announcement.getSearchFields().getEnginePowerToAsInteger()));
			//	stringBuilder.append("productionYearTo=" + announcement.getSearchFields().getProductionYearTo() + "&");
		}
		
		
		if(announcement.getVehicleType() != VehicleType.CAR)
			return stringBuilder.toString();
		
		
		if(announcement.getSearchFields().getDoorsFrom() != null) {
			queryBuilder.and(QAnnouncement.announcement.doors.goe(announcement.getSearchFields().getDoorsFrom()));
			//	stringBuilder.append("productionYearFrom=" + announcement.getSearchFields().getProductionYearFrom() + "&");
		}
		if(announcement.getSearchFields().getDoorsTo() != null) {
			queryBuilder.and(QAnnouncement.announcement.doors.lt(announcement.getSearchFields().getDoorsTo()));
			//	stringBuilder.append("productionYearTo=" + announcement.getSearchFields().getProductionYearTo() + "&");
		}
		
		return stringBuilder.toString();
	}

}
