package com.app.controller;

import com.app.dto.AnnouncementDto;
import com.app.dto.UserDto;
import com.app.entity.Announcement;
import com.app.entity.Picture;
import com.app.enums.VehicleType;
import com.app.repository.AnnouncementRepository;
import com.app.repository.ObservedAnnouncementRepository;
import com.app.repository.UserRepository;
import com.app.searchform.SearchStrategy;
import com.app.service.AnnouncementService;
import com.app.service.EmailService;
import com.app.utils.AnnouncementBreadCrumbService;
import com.app.utils.EmailMessage;
import com.app.utils.PaginationDetails;
import com.app.utils.Result;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.context.MessageSource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Controller
@RequestMapping("announcement/")
public class AnnouncementController {
	
	private final AnnouncementService announcementService;


	private final AnnouncementRepository announcementRepository;

	private final UserRepository userRepository;

	private final EmailService emailService;

	private final MessageSource messageSource;

	private final SearchStrategy<Announcement> announcementSearchStrategy;

	private final ObservedAnnouncementRepository observedAnnouncementRepository;

	private final ModelMapper modelMapper;

	private final AnnouncementBreadCrumbService breadCrumb;

	public AnnouncementController(AnnouncementService announcementService, AnnouncementRepository announcementRepository, UserRepository userRepository, EmailService emailService, MessageSource messageSource, SearchStrategy<Announcement> announcementSearchStrategy, ObservedAnnouncementRepository observedAnnouncementRepository, ModelMapper modelMapper, AnnouncementBreadCrumbService breadCrumb) {
		this.announcementService = announcementService;
		this.announcementRepository = announcementRepository;
		this.userRepository = userRepository;
		this.emailService = emailService;
		this.messageSource = messageSource;
		this.announcementSearchStrategy = announcementSearchStrategy;
		this.observedAnnouncementRepository = observedAnnouncementRepository;
		this.modelMapper = modelMapper;
		this.breadCrumb = breadCrumb;
	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String create(Model model, Authentication authentication) {
		Announcement announcement = Announcement.builder().active(true).vehicleType(VehicleType.CAR).build();
		announcement.setUser(userRepository.findByLogin(authentication.getName()));
		model.addAllAttributes(announcementSearchStrategy.prepareDataForHtmlElements(announcement));
		model.addAttribute("announcement", convertToDto(announcement));

		return "announcement/announcementEdit";
	}

	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public String get(@NotNull @Valid @PathVariable("id") Long id, Model model, Authentication authentication) {
		announcementRepository.findById(id).ifPresentOrElse(announcement -> {
			model.addAttribute("breadCrumb", breadCrumb.create(announcement));
			model.addAttribute("announcement", convertToDto(announcement));
			List<AnnouncementDto> otherUserAnnouncements =
					announcementRepository.findOtherUserAnnouncements(announcement.getId(), announcement.getUser().getId()).stream().map(this::convertToDto).collect(Collectors.toList());
			model.addAttribute("otherUserAnnouncements", otherUserAnnouncements);

			if (authentication == null)
				model.addAttribute("observedAnnouncement", false);
			else
				model.addAttribute("observedAnnouncement", observedAnnouncementRepository.existsByUserLoginAndAnnouncement(authentication.getName(), id));
		}, () -> {
			throw new ObjectNotFoundException(id, "Announcement");
		});

		return "announcement/announcementRead";
	}

	private AnnouncementDto convertToDto(Announcement announcement) {
		AnnouncementDto announcementDto = modelMapper.map(announcement, AnnouncementDto.class);
		announcementDto.setUserDto(modelMapper.map(announcement.getUser(), UserDto.class));

		return announcementDto;
	}

	private Announcement convertToEntity(AnnouncementDto announcementDto) {
		announcementDto.preparePictures();
		Announcement announcement = modelMapper.map(announcementDto, Announcement.class);
		announcement.setUser(userRepository.findById(announcementDto.getUserDto().getId()).get());
		List<Picture> pictures = announcementDto.getPictures().stream().map(e -> modelMapper.map(e, Picture.class)).collect(Collectors.toList());
		pictures.forEach(e -> e.setAnnouncement(announcement));

		if (pictures.stream().noneMatch(e -> e.isMainPhotoInAnnouncement()))
			pictures.stream().findAny().ifPresent(e -> e.setMainPhotoInAnnouncement(true));

		announcement.setPictures(pictures);
		announcement.setUser(userRepository.findById(announcementDto.getUserDto().getId()).get());

		return announcement;
	}

	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public String edit(@NotNull @Valid @PathVariable("id") Long id, Model model, Authentication authentication) {
		announcementRepository.findById(id).ifPresentOrElse(
				announcement -> {
					if (authentication.getAuthorities().stream().anyMatch(e -> e.getAuthority().equals("ROLE_ADMIN")) || announcement.getUser().getLogin().equals(authentication.getName())) {
						model.addAllAttributes(announcementSearchStrategy.prepareDataForHtmlElements(announcement));
						model.addAttribute("announcement", convertToDto(announcement));
						model.addAttribute("breadCrumb", breadCrumb.create(announcement));
					} else
						throw new AccessDeniedException("Access denied");
				},
				() -> {
					throw new ObjectNotFoundException(id, "Announcement");
				}
		);
		return "announcement/announcementEdit";
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public ModelAndView create(@ModelAttribute("announcement") @Validated AnnouncementDto announcementDto,
							   BindingResult bindingResult) {
		ModelAndView model = new ModelAndView("announcement/announcementEdit");
		Announcement announcement = convertToEntity(announcementDto);

		if (bindingResult.hasErrors()) {
			model.getModelMap().addAllAttributes(announcementSearchStrategy.prepareDataForHtmlElements(announcement));
			return model;
		} else {
			Result<Announcement> result = announcementService.saveAnnouncement(announcement);
			result.ifError(e -> {
				e.convertToMvcError(bindingResult);
				model.getModelMap().addAllAttributes(announcementSearchStrategy.prepareDataForHtmlElements(announcement));
			});
			result.ifSuccess(() -> {
				model.setViewName("redirect:/announcement/edit/" + announcement.getId());
			});
		}

		return model;
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public ModelAndView update(@ModelAttribute("announcement") @Validated AnnouncementDto announcementDto,
							   BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		Announcement announcement = convertToEntity(announcementDto);
		ModelAndView model = new ModelAndView("redirect:/announcement/edit/" + announcement.getId());


		if (bindingResult.hasErrors()) {
			prepareFormWithError(announcement, model);
			model.getModelMap().addAttribute("breadCrumb", breadCrumb.create(announcementDto));
			return model;
		} else {
			Result<Announcement> result = announcementService.saveAnnouncement(announcement);
			if (result.isError()) {
				prepareFormWithError(announcement, model);
				model.getModelMap().addAttribute("breadCrumb", breadCrumb.create(announcementDto));
				result.convertToMvcError(bindingResult);
				return model;
			}
		}
		redirectAttributes.addFlashAttribute("message", "changesSaved");

		return model;
	}

	private void prepareFormWithError(Announcement announcement, ModelAndView model) {
		model.setViewName("announcement/announcementEdit");
		model.getModelMap().addAllAttributes(announcementSearchStrategy.prepareDataForHtmlElements(announcement));
		model.getModelMap().addAttribute("message", "incorrectData");
		//	model.getModelMap().addAttribute("breadCrumb", breadCrumb.create(announcement));
	}

	@RequestMapping(value = "/list")
	public String list(@RequestParam(name = "page", defaultValue = "1", required = false) int page,
					   @RequestParam(name = "size", required = false, defaultValue = "10") int size,
					   @RequestParam(name = "orderBy", required = false, defaultValue = "id") String orderBy,
					   @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort,
					   @ModelAttribute("announcement") Announcement announcement,
					   Model model) {

		model.addAttribute("requestMapping", "list");

		if(announcement.getVehicleType() == null)
			announcement.setVehicleType(VehicleType.CAR);

		PaginationDetails paginationDetails = PaginationDetails.builder().page(page).size(size).orderBy(orderBy).sort(sort).build();

		model.addAllAttributes(announcementSearchStrategy.prepareSearchForm(announcement, paginationDetails));

		return "/announcement/announcementList";
	}

	@RequestMapping(value="/my")
	public String userAnnouncements(@RequestParam(name = "page", defaultValue = "1", required = false) int page,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "orderBy", required = false, defaultValue = "id") String orderBy,
			@RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort,
			@ModelAttribute("announcement")  Announcement announcement,
			Model model) {
		announcement.setUser(userRepository.findByLogin(getContext().getAuthentication().getName()));
		//	model.addAttribute("requestMapping", "my");

		PaginationDetails paginationDetails = PaginationDetails.builder().page(page).size(size).orderBy(orderBy).sort(sort).build();

		model.addAllAttributes(announcementSearchStrategy.prepareSearchForm(announcement, paginationDetails));

		return "/announcement/announcementList";
	}


	@RequestMapping(value = "sentMessageToSeller", method = RequestMethod.POST, consumes = "application/json;")
	public @ResponseBody
	Boolean sentMessageToSeller(@RequestBody String jsonStr, HttpServletRequest request) throws JSONException {
		JSONObject json = new JSONObject(jsonStr);

		String emailSubject = messageSource.getMessage("sendMessageToSellerSubject", new Object[]{}, Locale.getDefault());
		StringBuilder emailText = new StringBuilder(messageSource.getMessage("sendMessageToSellerContent", new Object[]{}, Locale.getDefault()));
		emailText.append(request.getRequestURL().toString().replace("sentMessageToSeller", "")).append(json.getInt("announcementId")).append(" ");
		emailText.append("\n");
		emailText.append(json.getString("messageText"));

		EmailMessage emailToSend = EmailMessage.builder().
				subject(emailSubject).
				content(emailText.toString()).
				senderEmail(json.getString("customerEmail")).
				receiverEmailsAddress(json.getString("sellerEmail")).
				build();

		Result sentResult = emailService.sendEmail(emailToSend);

		return sentResult.isSuccess();
	}

	@RequestMapping(value = "reportAnnouncement", method = RequestMethod.POST)
	public @ResponseBody
	Boolean reportAnnouncement(@RequestBody String jsonStr) throws JSONException {
		JSONObject json = new JSONObject(jsonStr);

		String reportText = json.getString("reportText");
		Long announcementId = json.getLong("announcementId");

		return StringUtils.isNotBlank(reportText) && announcementId != null;
	}

}
