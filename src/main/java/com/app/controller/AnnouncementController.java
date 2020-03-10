package com.app.controller;

import com.app.entity.Announcement;
import com.app.entity.VehicleModel;
import com.app.enums.PageSize;
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
import com.app.utils.BreadCrumb;
import com.app.utils.HtmlElement;
import com.app.utils.Result;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.querydsl.core.types.Predicate;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

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

	@Autowired
	MessageSource messageSource;

	@Value("${system.email.address}")
	private String systemEmail;
	
	String repositoryLocation;
	
	@PostConstruct
	public void init() {
		repositoryLocation = environment.getProperty("spring.repository.location");
	}
	
	@RequestMapping(value="create",method=RequestMethod.GET)
	public String create(Model model) {
		VehicleType vehicleType = VehicleType.CAR;

		List<ManufacturerProjection> manufacturerList = manufacturerRepository.findByVehicleType(vehicleType);
		Announcement announcement = Announcement.newInstanceForAnnouncementCreationForm();
		announcement.setUser(userRepository.findByLogin(getContext().getAuthentication().getName()));

		model.addAttribute("announcement", announcement);
		model.addAttribute("manufacturerList", manufacturerList);
		model.addAttribute("vehicleSubtypeList",VehicleSubtype.vehicleSubtypesWithLabels(vehicleType));

		if(!manufacturerList.isEmpty()) {
			model.addAttribute("vehicleModelList", vehicleModelRepository.findByManufacturerId(manufacturerList.get(0).getId()));
		}


		return "announcement/announcementEdit";
	}
	
	@RequestMapping(value="read/{id}",method=RequestMethod.GET)
	public String read(@PathVariable("id") Long id, Model model) {
		Optional<Announcement> announcement = announcementRepository.findById(id);

		model.addAttribute("breadCrumb", BreadCrumb.create(announcement.get()));
		model.addAttribute("announcement", announcement.get());
		model.addAttribute("otherUserAnnouncements", announcementRepository.findFirst5ByUserIdAndOtherThenAnnouncementIdFetchPicturesEagerly(announcement.get().getId(), announcement.get().getUser().getId()));
		
		return "announcement/announcementRead";
	}
	
	@RequestMapping(value="edit/{id}",method=RequestMethod.GET)
	public String edit(@PathVariable("id") Long id, Model model) {
		Optional<Announcement> announcement = announcementRepository.findById(id);
		
		if(announcement.isPresent()) {
			VehicleType vehicleType = VehicleType.CAR;
			List<ManufacturerProjection> manufacturerList = manufacturerRepository.findByVehicleType(announcement.get().getVehicleModel().getVehicleType());
			model.addAttribute("manufacturerList", manufacturerList);
			model.addAttribute("vehicleSubtypeList",VehicleSubtype.vehicleSubtypesWithLabels(announcement.get().getVehicleModel().getVehicleType()));

			
		
			if(model.asMap().containsKey("error")) {
				model.addAttribute("error", model.asMap().get("error"));
				model.addAttribute("announcement", model.asMap().get("announcement"));
			}
			else {
			//	announcement.get().setManufacturerId(announcement.get().getVehicleModel().getManufacturer().getId());
				announcement.get().setManufacturerId(announcement.get().getVehicleModel().getManufacturer().getId());
				model.addAttribute("announcement", announcement.get());
			}

			if(!manufacturerList.isEmpty())
				model.addAttribute("vehicleModelList", vehicleModelRepository.findByManufacturerId(announcement.get().getVehicleModel().getManufacturer().getId()));
		}
		
		return "announcement/announcementEdit";
	}
	
	
	private void deleteImagesFromRepository(List<String> imageNames) {
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

		model.addAttribute("requestMapping", "list");
		loadDataAndPrepareModelForList(page, size, orderBy, sort, searchArguments, announcement, model);

		return "/announcement/announcementList";
	}

	@RequestMapping(value="/my")
	public String userAnnouncements(@RequestParam(name = "page", required = false) Optional<Integer> page,
			@RequestParam(name = "size", required = false, defaultValue = "10") Optional<Integer> size,
			@RequestParam(name = "orderBy", required = false, defaultValue = "id") Optional<String> orderBy,
			@RequestParam(name = "sort", required = false, defaultValue = "ASC") Optional<String> sort,
			@RequestParam(name = "searchArguments",required = false, defaultValue = "&") String searchArguments,
			@ModelAttribute("announcement")  Announcement announcement,
			Model model) {

		announcement.setUser(userRepository.findByLogin(getContext().getAuthentication().getName()));
		model.addAttribute("requestMapping", "my");

		loadDataAndPrepareModelForList(page, size, orderBy, sort, searchArguments, announcement, model);

		return "/announcement/announcementList";
	}

	@RequestMapping(value="/observed")
	public String announcementObservedByUser(@RequestParam(name = "page", required = false) Optional<Integer> page,
									@RequestParam(name = "size", required = false, defaultValue = "10") Optional<Integer> size,
									@RequestParam(name = "orderBy", required = false, defaultValue = "id") Optional<String> orderBy,
									@RequestParam(name = "sort", required = false, defaultValue = "ASC") Optional<String> sort,
									@RequestParam(name = "searchArguments",required = false, defaultValue = "&") String searchArguments,
									@ModelAttribute("announcement")  Announcement announcement,
									Model model) {

		announcement.setUser(userRepository.findByLogin(getContext().getAuthentication().getName()));
		loadDataAndPrepareModelForList(page, size, orderBy, sort, searchArguments, announcement, model);
		return "/announcement/announcementList";
	}

	private void loadDataAndPrepareModelForList(@RequestParam(name = "page", required = false) Optional<Integer> page, @RequestParam(name = "size", required = false, defaultValue = "10") Optional<Integer> size, @RequestParam(name = "orderBy", required = false, defaultValue = "id") Optional<String> orderBy, @RequestParam(name = "sort", required = false, defaultValue = "ASC") Optional<String> sort, @RequestParam(name = "searchArguments", required = false, defaultValue = "&") String searchArguments, @ModelAttribute("announcement") Announcement announcement, Model model) {
		List<Predicate> predicates = Lists.newArrayList();
		searchArguments = announcementQueryPreparer.prepareQueryAndSearchArguments(announcement, searchArguments, predicates);

		PageRequest pageRequest = PageRequest.of(page.orElse(1) - 1, size.orElse(10), Direction.fromString(sort.orElse("ASC")), orderBy.orElse("id"));
		Page<Announcement> announcementPages = announcementRepository.findByPredicatesAndLoadPicturesForPagination(pageRequest, predicates.stream().toArray(Predicate[]::new));

		setModelAttributesWithData(searchArguments, announcement, model, announcementPages);
		setModelAttributesForPagination(page, size, orderBy, sort, model, announcementPages.getTotalPages());
		setModelAttributesFromEnums(model);
	}

	private void setModelAttributesWithData(String searchArguments, Announcement announcement, Model model, Page<Announcement> announcementPages) {
		if(announcement.getVehicleType() == null)
			announcement.setVehicleType(VehicleType.CAR);

		List<ManufacturerProjection> manufacturerList = manufacturerRepository.findByVehicleType(announcement.getVehicleType());
		model.addAttribute("manufacturerList", manufacturerList);
		setVehicleModelsIfManufacturerIsSet(announcement, model, manufacturerList);

		model.addAttribute("vehicleSubtypeList", VehicleSubtype.vehicleSubtypesWithLabels(announcement.getVehicleType()));
		model.addAttribute("searchArguments", searchArguments);
		model.addAttribute("announcement", announcement);
		model.addAttribute("pages", announcementPages);
	}

	private void setVehicleModelsIfManufacturerIsSet(@ModelAttribute("announcement") Announcement announcement, Model model, List<ManufacturerProjection> manufacturerList) {
		if(announcement.getManufacturerId() != null) {
			announcement.setManufacturerName(manufacturerList.stream().filter(e -> e.getId() == announcement.getManufacturerId()).findAny().get().getName());
			model.addAttribute("vehicleModelList", vehicleModelRepository.findByManufacturerIdAndVehicleType(announcement.getManufacturerId(), announcement.getVehicleType()));
		}
	}

	private void setModelAttributesForPagination(Optional<Integer> page, Optional<Integer> size, Optional<String> orderBy, Optional<String> sort, Model model, int totalPages) {
		model.addAttribute("page", page.orElse(1));
		model.addAttribute("orderBy", orderBy.orElse(""));
		model.addAttribute("sort", sort.orElse("ASC"));
		model.addAttribute("size", size.orElse(10));

		if(totalPages > 0) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1 ,totalPages).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}
	}

	private void setModelAttributesFromEnums(Model model) {
		model.addAttribute("pageSizes", PageSize.LIST);
		model.addAttribute("pricesList", SearchEngineDropDownValues.CAR_PRICES_LIST);
		model.addAttribute("mileageList", SearchEngineDropDownValues.MILEAGE_LIST);
		model.addAttribute("engineCapacityList", SearchEngineDropDownValues.ENGINE_CAPACITY_LIST);
		model.addAttribute("enginePowerList", SearchEngineDropDownValues.ENGINE_POWER_LIST);
		model.addAttribute("doorsList", SearchEngineDropDownValues.DOOR_LIST);
	}


	private void String() {

	}

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
