package com.app.controller;

import com.app.entity.Announcement;
import com.app.entity.Picture;
import com.app.enums.VehicleType;
import com.app.repository.AnnouncementRepository;
import com.app.repository.ObservedAnnouncementRepository;
import com.app.repository.UserRepository;
import com.app.searchForm.SearchFormStrategy;
import com.app.service.AnnouncementService;
import com.app.service.EmailService;
import com.app.service.PictureService;
import com.app.utils.BreadCrumb;
import com.app.utils.EmailMessage;
import com.app.utils.PaginationDetails;
import com.app.utils.Result;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Controller
@RequestMapping("announcement/")
public class AnnouncementController {
	
	private final AnnouncementService announcementService;

	private final PictureService pictureService;
	
	private final AnnouncementRepository announcementRepository;
	
	private final UserRepository userRepository;

	private final EmailService emailService;
	
	private final MessageSource messageSource;

	@Autowired
	private SearchFormStrategy<Announcement> announcementSearchFormStrategy;

	public AnnouncementController(AnnouncementService announcementService, PictureService pictureService, AnnouncementRepository announcementRepository, UserRepository userRepository, EmailService emailService, MessageSource messageSource, ObservedAnnouncementRepository observedAnnouncementRepository) {
		this.announcementService = announcementService;
		this.pictureService = pictureService;
		this.announcementRepository = announcementRepository;
		this.userRepository = userRepository;
		this.emailService = emailService;
		this.messageSource = messageSource;
	}

	@RequestMapping(value="create",method=RequestMethod.GET)
	public String create(Model model) {
		VehicleType vehicleType = VehicleType.CAR;

		Announcement announcement = Announcement.builder().build();
		announcement.setVehicleType(VehicleType.CAR);
		announcement.setUser(userRepository.findByLogin(getContext().getAuthentication().getName()));
		model.addAttribute("announcement", announcement);

		model.addAllAttributes(announcementSearchFormStrategy.prepareDataForHtmlElements(announcement));

		return "announcement/announcementEdit";
	}

	@RequestMapping(value="read/{id}",method=RequestMethod.GET)
	public String read(@NotNull @Valid @PathVariable("id") Long id, Model model) {

		 announcementRepository.findById(id).ifPresentOrElse(announcement -> {
			model.addAttribute("breadCrumb", BreadCrumb.create(announcement));
			model.addAttribute("announcement", announcement);
			List<Announcement> other5UserAnnouncements = announcementRepository.findFirst5ByUserIdAndOtherThenIdFetchPictures(announcement.getId(), announcement.getUser().getId());
			model.addAttribute("otherUserAnnouncements", other5UserAnnouncements);
		}, () -> {
			 throw new ObjectNotFoundException(id, "Announcement");
		 });

		return "announcement/announcementRead";
	}
	
	@RequestMapping(value="edit/{id}", method=RequestMethod.GET)
	public String edit(@NotNull @Valid @PathVariable("id") Long id, Model model) {

		if(model.asMap().containsKey("errorDuringSave")) {
			Announcement announcementWithError = (Announcement) model.getAttribute("announcement");
		//	model.addAllAttributes(announcementSearchAndPaginationPreparer.prepareValuesForSelects(announcementWithError.getVehicleModel().getVehicleType(), Optional.of(announcementWithError.getVehicleModel().getManufacturer().getId())));
			model.addAllAttributes(announcementSearchFormStrategy.prepareDataForHtmlElements(announcementWithError));
		}
		else
			announcementRepository.findById(id).ifPresentOrElse(
					announcement -> {
						model.addAllAttributes(announcementSearchFormStrategy.prepareDataForHtmlElements(announcement));
						rewriteManufacturerIdFromVehicleModelForSearchForm(announcement);
						model.addAttribute("announcement", announcement);
					},
					() -> {
						throw new ObjectNotFoundException(id, "Announcement");
					}
			);

		return "announcement/announcementEdit";
	}

	@RequestMapping(value="save",method=RequestMethod.POST)
	public String save(@ModelAttribute("announcement") @Validated Announcement announcement,
			BindingResult bindingResult, Model model, Authentication authentication,
			RedirectAttributes redirectAttributes) {

		announcement = preparePicturesToSaveAndDelete(announcement);

		Optional<Result> saveResult = Optional.empty();

		if(!bindingResult.hasErrors()) {
			saveResult = Optional.of(announcementService.saveAnnouncement(announcement));
		}

		if(bindingResult.hasErrors() || saveResult.orElse(Result.Error()).isError()) {
			prepareFlashAttributesForErrorHandling(announcement, bindingResult, redirectAttributes);
			return "redirect:/announcement/edit/" + announcement.getId();
		}
		else {
			pictureService.deleteFromFileRepository(announcement.getImagesToDelete());
		}

		return "redirect:/announcement/list";
	}

	private Announcement preparePicturesToSaveAndDelete(@Validated @ModelAttribute("announcement") Announcement announcement) {
		Map<Boolean,List<Picture>> picturesToSaveAndDeleteInSeparateLists = announcement.getPictures().stream().collect(Collectors.partitioningBy(Picture::isPictureToDelete));
		List<Picture> imagesToDelete = picturesToSaveAndDeleteInSeparateLists.get(Boolean.TRUE);
		List<Picture> imagesToSave = picturesToSaveAndDeleteInSeparateLists.get(Boolean.FALSE);
		imagesToSave = setAnnouncementForNewPictures(imagesToSave, announcement);

		announcement.setPictures(imagesToSave);
		announcement.setImagesToDelete(imagesToDelete);

		return announcement;
	}

	@RequestMapping(value="/list")
	public String list(@RequestParam(name = "page", defaultValue = "1",  required = false) int page,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "orderBy", required = false, defaultValue = "id") String orderBy,
			@RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort,
			@ModelAttribute("announcement")  Announcement announcement,
			Model model) {

		model.addAttribute("requestMapping", "list");

		if(announcement.getVehicleType() == null)
			announcement.setVehicleType(VehicleType.CAR);

		PaginationDetails paginationDetails = PaginationDetails.builder().page(page).size(size).orderBy(orderBy).sort(sort).build();

		model.addAllAttributes(announcementSearchFormStrategy.prepareSearchForm(announcement, paginationDetails));

		return "/announcement/announcementList";
	}



	@RequestMapping(value="/my")
	public String userAnnouncements(@RequestParam(name = "page", defaultValue = "1", required = false) int page,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "orderBy", required = false, defaultValue = "id") String orderBy,
			@RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort,
			@RequestParam(name = "searchArguments",required = false, defaultValue = "&") String searchArguments,
			@ModelAttribute("announcement")  Announcement announcement,
			Model model) {

		announcement.setUser(userRepository.findByLogin(getContext().getAuthentication().getName()));
		model.addAttribute("requestMapping", "my");

		PaginationDetails paginationDetails = PaginationDetails.builder().page(page).size(size).orderBy(orderBy).sort(sort).build();
	//	model.addAllAttributes(announcementSearchAndPaginationPreparer.loadDataAndPrepareModelForPagination(paginationDetails, announcement));

		model.addAllAttributes(announcementSearchFormStrategy.prepareSearchForm(announcement, paginationDetails));

		return "/announcement/announcementList";
	}


	@RequestMapping(value="sentMessageToSeller", method = RequestMethod.GET)
	public @ResponseBody Boolean sentMessageToSeller(@RequestParam("announcementId") Long announcementId,
			@RequestParam("messageText") String messageText,
			@RequestParam("email") String email, @RequestParam("sellerEmailAddress") String sellerEmailAddress, Model model, RedirectAttributes redirectAttributes) {
		
		String emailSubject = messageSource.getMessage("sendMessageToSellerSubject", new Object[] {announcementId} ,Locale.getDefault());
		String emailText = messageSource.getMessage("sendMessageToSellerContent", new Object[] {messageText}, Locale.getDefault());

		EmailMessage emailToSend = EmailMessage.builder().
				subject(emailSubject).
				content(emailText).
				senderEmail(email).
				receiverEmailsAddress(sellerEmailAddress).
				build();

		Result sentResult = emailService.sendEmail(emailToSend);

		return sentResult.isSuccess();
	}
	
	@RequestMapping(value="reportAnnouncement", method = RequestMethod.GET)
	public @ResponseBody Boolean reportAnnouncement(@RequestParam("announcementId") Long announcementId,
			@RequestParam("reportText") String reportText,
			@RequestParam("email") String email , Model model, RedirectAttributes redirectAttributes) {
		
		String emailSubject = messageSource.getMessage("reportAnnouncementEmailSubject", new Object[] {announcementId} ,Locale.getDefault());
		String emailText = messageSource.getMessage("reportAnnouncementEmailContent", new Object[] {reportText}, Locale.getDefault());

		EmailMessage emailToSend = EmailMessage.builder().
				subject(emailSubject).
				content(emailText).
				senderEmail(email).
				build();

		Result sentResult = emailService.sendEmail(emailToSend);
		
		return sentResult.isSuccess();
	}

	private Map<String, Announcement> prepareNewAnnouncement() {
		Announcement announcement = Announcement.builder().build();
		announcement.setUser(userRepository.findByLogin(getContext().getAuthentication().getName()));

		return Map.of("announcement", announcement);
	}

	private void rewriteManufacturerIdFromVehicleModelForSearchForm(Announcement announcement) {
		announcement.setManufacturerId(announcement.getVehicleModel().getManufacturer().getId());
	}

	private void prepareFlashAttributesForErrorHandling(@Validated @ModelAttribute("announcement") Announcement announcement, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("errorDuringSave");
		redirectAttributes.addFlashAttribute("announcement", announcement);
		redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.announcement", bindingResult);
	}

	private List<Picture> setAnnouncementForNewPictures(List<Picture> pictures, Announcement announcement) {
		for (Picture picture : pictures) {
			if(picture == null)
				picture.setAnnouncement(announcement);
		}

		return pictures;
	}

}