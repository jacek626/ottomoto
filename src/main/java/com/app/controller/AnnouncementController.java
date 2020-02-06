package com.app.controller;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

import com.app.entity.Announcement;
import com.app.entity.QAnnouncement;
import com.app.entity.QPicture;
import com.app.entity.VehicleModel;
import com.app.enums.BooleanValuesForDropDown;
import com.app.enums.CarColor;
import com.app.enums.FuelType;
import com.app.enums.SearchEngineDropDownValues;
import com.app.enums.VehicleSubtype;
import com.app.enums.VehicleType;
import com.app.projection.ManufacturerProjection;
import com.app.repository.AnnouncementRepository;
import com.app.repository.ManufacturerRepository;
import com.app.repository.UserRepository;
import com.app.repository.VehicleModelRepository;
import com.app.service.AnnouncementService;
import com.app.service.EmailService;
import com.app.utils.AnnouncementQueryPreparer;
import com.app.utils.BreadCrumbElement;
import com.app.utils.HtmlElement;
import com.app.utils.Result;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

@Controller
@RequestMapping("announcement/")
public class AnnouncementController {
	
	@Autowired
	private AnnouncementService announcementService;
	
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
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private AnnouncementQueryPreparer announcementQueryPreparer;
	
	
	@Value("${system.email.address}")
	private String systemEmail;
	
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
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		Announcement announcement = new Announcement();
		announcement.setUser(userRepository.findByLogin(authentication.getName()));
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
	
	@RequestMapping(value="show/{id}",method=RequestMethod.GET)
	public String show(@PathVariable("id") Long id, Model model) {
		Optional<Announcement> announcement = announcementRepository.findById(id);
		
		BreadCrumbElement breadCrumbElement0 = new BreadCrumbElement(announcement.get().getVehicleModel().getVehicleType().getLabel(), "vehicleType=" + announcement.get().getVehicleModel().getVehicleType()); 
		BreadCrumbElement breadCrumbElement1 = new BreadCrumbElement(announcement.get().getVehicleModel().getManufacturer().getName(), "manufacturer=" + announcement.get().getVehicleModel().getManufacturer().getId());
		BreadCrumbElement breadCrumbElement2 = new BreadCrumbElement(announcement.get().getVehicleModel().getName(), "vehicleModel=" + announcement.get().getVehicleModel().getId());
		model.addAttribute("breadCrumb", Lists.newArrayList(breadCrumbElement0, breadCrumbElement1, breadCrumbElement2));
		
		model.addAttribute("announcement", announcement.get());
		model.addAttribute("otherUserAnnouncements", announcementRepository.findFirst5ByUserIdAndOtherThenAnnouncementIdFetchPicturesEagerly(announcement.get().getId(), announcement.get().getUser().getId()));
		
		return "announcement/announcementShow";
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
				model.addAttribute("vehicleModelList", vehicleModelRepository.findByManufacturerId(announcement.get().getVehicleModel().getManufacturer().getId()));
		}
		
		return "announcement/announcementEdit";
	}
	
	
	private void deleteImagesFromRepository(List<String> imageNames) {
		String repositoryLocation = environment.getProperty("spring.repository.location");
		
		for (String name : imageNames) {
		 	 Paths.get(repositoryLocation, name).toFile().delete();
		}
	}
	
	
	@RequestMapping(value="save",method=RequestMethod.POST)    // to jest za edycje
	public String save(@ModelAttribute("announcement") @Validated Announcement announcement,
			BindingResult bindingResult, Errors errors, Model model, Authentication authentication,
			RedirectAttributes redirectAttributes) {
		
		handlingDeletedImages(announcement);
		setAnnouncementForNewImages(announcement);
		
		if(errors.hasErrors() ||  bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute("announcement",announcement);
			redirectAttributes.addFlashAttribute("error",errors);
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.announcement", bindingResult);
			return "redirect:/announcement/edit/" + announcement.getId();
		}
		
		Result saveResult = announcementService.saveAnnouncement(announcement);
		
		if(saveResult.isError()) {
			redirectAttributes.addFlashAttribute("announcement",announcement);
			redirectAttributes.addFlashAttribute("error",errors);
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.announcement", bindingResult);
			return "redirect:/announcement/edit/" + announcement.getId();
		}
		
		
		return "redirect:/announcement/list";
	}

	private void setAnnouncementForNewImages(Announcement announcement) {
		announcement.getPictures().stream().filter(p -> p.getAnnouncement() == null).forEach(p -> p.setAnnouncement(announcement));
	}

	private void handlingDeletedImages(Announcement announcement) {
		if(announcement.getPictures() != null) {
			deleteImagesFromRepository(announcement.getPictures().stream().filter(i -> i.isPictureToDelete()).map(p -> p.getRepositoryName()).collect(Collectors.toList()));
			announcement.setPictures(announcement.getPictures().stream().filter(i -> !i.isPictureToDelete()).collect(Collectors.toList()));
		}
	}

	
	@RequestMapping(value="loadManufacturer",method=RequestMethod.GET)
	public @ResponseBody String loadManufacturer(
			@RequestParam("vehicleType") String vehicleType,
			@RequestParam("typeOfHtmlElement") String htmlElement, Model model,
			Authentication authentication, ModelMap map) {
	
		List<ManufacturerProjection> manufacturerList = manufacturerRepository.findByVehicleType(VehicleType.valueOf(vehicleType));
		
		
		String fromStream =  manufacturerList.stream()
				.map(e -> "<" + htmlElement + " value='" + e.getId() + "'>" + e.getName() + "</"+ htmlElement +">")
				.collect(Collectors.joining("")).toString();
		
		
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
		
	//	BooleanBuilder queryBuilder = new BooleanBuilder();
		List<Predicate> predictates = new ArrayList<>();
		searchArguments = announcementQueryPreparer.prepareQueryAndSearchArguments(announcement, searchArguments, predictates);
		
		PageRequest pageable = null;
		pageable = PageRequest.of(page.orElse(1) - 1, size.orElse(10), Direction.fromString(sort.orElse("ASC")), orderBy.orElse("id"));

		
	//	predictates.add(QPicture.picture.mainPhotoInAnnouncement.eq(true));
	//	announcementRepository.findByPredicatesAndLoadMainPicture(predicates)
	//	List<Announcement> announcementList = announcementRepository.findByPredicatesAndLoadPictures(pageable, QAnnouncement.announcement.title.eq("announcement title"), QPicture.picture.mainPhotoInAnnouncement.eq(true));
		Page<Announcement> announcementPages = announcementRepository.findByPredicatesAndLoadPicturesForPagination(pageable, predictates.stream().toArray(Predicate[]::new));
	//	Page<Announcement> announcementPage = announcementRepository.findAll(queryBuilder, pageable);
		
        if(announcementPages.getTotalPages() > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1 ,announcementPages.getTotalPages()).boxed().collect(Collectors.toList());
            model.addAttribute("numbers", pageNumbers);
        }
        
        model.addAttribute("vehicleTypeList", VehicleType.vehicleTypesWithLabels());
       
		List<ManufacturerProjection> manufacturerList = manufacturerRepository.findByVehicleType(announcement.getVehicleType());
		model.addAttribute("manufacturerList", manufacturerList);
        
        
        if(announcement.getVehicleModel() != null && announcement.getVehicleModel().getManufacturer() != null)
			model.addAttribute("vehicleModelList", vehicleModelRepository.findByManufacturerIdAndVehicleType(announcement.getVehicleModel().getManufacturer().getId(), announcement.getVehicleType()));
        
        model.addAttribute("vehicleSubtypeList",VehicleSubtype.vehicleSubtypesWithLabels2(announcement.getVehicleType()));
        
        
        model.addAttribute("fuelTypeList", FuelType.values());
        model.addAttribute("pages", announcementPages);
        model.addAttribute("orderBy", orderBy.orElse(""));
        model.addAttribute("sort", sort.orElse("ASC"));
        model.addAttribute("searchArguments", searchArguments);
        model.addAttribute("page", page.orElse(1));
        model.addAttribute("size", size.orElse(10));
        model.addAttribute("pageSizes", PAGE_SIZES);
        model.addAttribute("announcement", announcement);
        model.addAttribute("colorList", CarColor.values());
        model.addAttribute("booleanValuesForDropDown", BooleanValuesForDropDown.values());
        
        model.addAttribute("pricesList", SearchEngineDropDownValues.CAR_PRICES_LIST);
        model.addAttribute("mileageList", SearchEngineDropDownValues.MILEAGE_LIST);
        model.addAttribute("engineCapacityList", SearchEngineDropDownValues.ENGINE_CAPACITY_LIST);
        model.addAttribute("enginePowerList", SearchEngineDropDownValues.ENGINE_POWER_LIST);
        model.addAttribute("doorsList", SearchEngineDropDownValues.DOOR_LIST);
        
		return "/announcement/announcementList";
	}
	
	
	@Autowired
	MessageSource messageSource;
	
	
	@RequestMapping(value="sentMessageToSeller", method = RequestMethod.GET)
	public @ResponseBody String sentMessageToSeller(@RequestParam("announcementId") Long announcementId,
			@RequestParam("messageText") String messageText,
			@RequestParam("email") String email , Model model, RedirectAttributes redirectAttributes) {
		
		String emailTitle = messageSource.getMessage("reportAnnouncementEmailTitle", new Object[] {announcementId} ,Locale.getDefault());
		String emailText = messageSource.getMessage("reportEmailText", new Object[] {messageText}, Locale.getDefault());
		
		/*
		 * try { emailService.sendEmail(email, emailTitle, emailText, systemEmail); }
		 * catch (Exception e) { e.printStackTrace(); return Boolean.FALSE.toString(); }
		 */
		
		return Boolean.TRUE.toString();
	}
	
	@RequestMapping(value="reportAnnouncement", method = RequestMethod.GET)
	public @ResponseBody String reportAnnouncement(@RequestParam("announcementId") Long announcementId,
			@RequestParam("reportText") String reportText,
			@RequestParam("email") String email , Model model, RedirectAttributes redirectAttributes) {
		
		String emailTitle = messageSource.getMessage("reportAnnouncementEmailTitle", new Object[] {announcementId} ,Locale.getDefault());
		String emailText = messageSource.getMessage("reportEmailText", new Object[] {reportText}, Locale.getDefault());
		
		/*
		 * try { emailService.sendEmail(email, emailTitle, emailText, systemEmail); }
		 * catch (Exception e) { e.printStackTrace(); return Boolean.FALSE.toString(); }
		 */
		
		return Boolean.TRUE.toString();
	}

}
