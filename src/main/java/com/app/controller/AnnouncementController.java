package com.app.controller;

import com.app.dto.AnnouncementDto;
import com.app.entity.Announcement;
import com.app.enums.VehicleType;
import com.app.repository.AnnouncementRepository;
import com.app.repository.ObservedAnnouncementRepository;
import com.app.repository.UserRepository;
import com.app.searchform.SearchStrategy;
import com.app.service.AnnouncementBreadCrumbService;
import com.app.service.AnnouncementService;
import com.app.service.EmailService;
import com.app.utils.email.MessageToSellerData;
import com.app.utils.mapper.AnnouncementMapper;
import com.app.utils.search.PaginationDetails;
import com.app.utils.validation.Result;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.ObjectNotFoundException;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
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
import java.util.Date;
import java.util.stream.Collectors;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Controller
@RequestMapping("announcement/")
public class AnnouncementController {
	
	private final AnnouncementService announcementService;

	private final AnnouncementRepository announcementRepository;

	private final UserRepository userRepository;

	private final EmailService emailService;

	private final SearchStrategy<Announcement, AnnouncementDto> announcementSearchStrategy;

	private final ObservedAnnouncementRepository observedAnnouncementRepository;

	private final AnnouncementMapper announcementMapper;

	private final AnnouncementBreadCrumbService breadCrumb;

	public AnnouncementController(AnnouncementService announcementService, AnnouncementRepository announcementRepository, UserRepository userRepository, EmailService emailService, SearchStrategy<Announcement, AnnouncementDto> announcementSearchStrategy, ObservedAnnouncementRepository observedAnnouncementRepository, AnnouncementMapper announcementMapper, AnnouncementBreadCrumbService breadCrumb) {
		this.announcementService = announcementService;
		this.announcementRepository = announcementRepository;
		this.userRepository = userRepository;
		this.emailService = emailService;
		this.announcementSearchStrategy = announcementSearchStrategy;
		this.observedAnnouncementRepository = observedAnnouncementRepository;
		this.announcementMapper = announcementMapper;
		this.breadCrumb = breadCrumb;
	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String create(Model model, Authentication authentication) {
		Announcement announcement = Announcement.builder().active(true).vehicleType(VehicleType.CAR).build();
		announcement.setUser(userRepository.findByLogin(authentication.getName()));
		model.addAllAttributes(announcementSearchStrategy.prepareDataForHtmlElements(announcement));
		model.addAttribute("announcement", announcementMapper.convertToDto(announcement));

		return "announcement/announcementEdit";
	}

	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public String get(@NotNull @Valid @PathVariable("id") Long id, Model model, Authentication authentication) {
		announcementRepository.findById(id).ifPresentOrElse(announcement -> {
			var announcementDto = announcementMapper.convertToDto(announcement);
			model.addAttribute("breadCrumb", breadCrumb.create(announcementDto));
			model.addAttribute("announcement", announcementDto);
			var otherUserAnnouncements = announcementRepository.findOtherUserAnnouncements(announcementDto.getId(), announcementDto.getUser().getId()).stream().map(announcementMapper::convertToDto).collect(Collectors.toList());
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

	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public String edit(@NotNull @Valid @PathVariable("id") Long id, Model model, Authentication authentication) {
		announcementRepository.findById(id).ifPresentOrElse(announcement -> {
					if (authentication.getAuthorities().stream().anyMatch(e -> e.getAuthority().equals("ROLE_ADMIN")) || announcement.getUser().getLogin().equals(authentication.getName())) {
						var announcementDto = announcementMapper.convertToDto(announcement);
						model.addAllAttributes(announcementSearchStrategy.prepareDataForHtmlElements(announcement));
						model.addAttribute("announcement", announcementMapper.convertToDto(announcement));
						model.addAttribute("breadCrumb", breadCrumb.create(announcementDto));
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
		var model = new ModelAndView("announcement/announcementEdit");
		var announcement = announcementMapper.convertToEntity(announcementDto);
		announcement.setCreationDate(new Date());

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
		var announcement = announcementMapper.convertToEntity(announcementDto);
		var model = new ModelAndView("redirect:/announcement/edit/" + announcement.getId());


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
	}

	@RequestMapping(value = "/list")
	public String list(@RequestParam(name = "page", defaultValue = "1", required = false) int page,
					   @RequestParam(name = "size", required = false, defaultValue = "10") int size,
					   @RequestParam(name = "orderBy", required = false, defaultValue = "id") String orderBy,
					   @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort,
					   @ModelAttribute("announcement") Announcement announcement,
					   Model model) {
		model.addAttribute("requestMapping", "list");

		var paginationDetails = PaginationDetails.builder().page(page).size(size).orderBy(orderBy).sort(sort).build();
		model.addAllAttributes(announcementSearchStrategy.prepareSearchForm(announcement, paginationDetails));

		return "announcement/announcementList";
	}

	@RequestMapping(value="/my")
	public String userAnnouncements(@RequestParam(name = "page", defaultValue = "1", required = false) int page,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "orderBy", required = false, defaultValue = "id") String orderBy,
			@RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort,
			@ModelAttribute("announcement")  Announcement announcement,
			Model model) {
		model.addAttribute("requestMapping", "my");
		announcement.setUser(userRepository.findByLogin(getContext().getAuthentication().getName()));
		PaginationDetails paginationDetails = PaginationDetails.builder().page(page).size(size).orderBy(orderBy).sort(sort).build();
		model.addAllAttributes(announcementSearchStrategy.prepareSearchForm(announcement, paginationDetails));

		return "announcement/announcementList";
	}


	@RequestMapping(value = "sentMessageToSeller", method = RequestMethod.POST, consumes = "application/json;")
	public @ResponseBody
	Boolean sentMessageToSeller(@RequestBody String jsonStr, HttpServletRequest request) throws JSONException {
		JSONObject json = new JSONObject(jsonStr);
		var messageToSellerData = MessageToSellerData.builder()
				.requestUrl(request.getRequestURL().toString().replace("sentMessageToSeller", ""))
				.announcementId(json.getInt("announcementId"))
				.customerEmail(json.getString("customerEmail"))
				.sellerEmail(json.getString("sellerEmail"))
				.messageText(json.getString("messageText"))
				.build();

		Result sentResult = emailService.sentMessageToSeller(messageToSellerData);

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
