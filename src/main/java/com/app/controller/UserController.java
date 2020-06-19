package com.app.controller;

import com.app.entity.User;
import com.app.repository.UserRepository;
import com.app.searchform.SearchStrategy;
import com.app.service.EmailService;
import com.app.service.UserService;
import com.app.utils.PaginationDetails;
import com.app.utils.Result;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;
import java.util.Optional;


@Controller
@RequestMapping("user/")
public class UserController {

	private final UserRepository userRepository;

	private final EmailService emailService;

	private final UserService userService;

	private final SearchStrategy<User> userSearchStrategy;

	public UserController(SearchStrategy<User> userSearchStrategy, UserRepository userRepository, EmailService emailService, UserService userService) {
		this.userSearchStrategy = userSearchStrategy;
		this.userRepository = userRepository;
		this.emailService = emailService;
		this.userService = userService;
	}

	@RequestMapping(value = "registration/{returnPage}", method = RequestMethod.GET)
	public String register(Model model, @PathVariable(name = "returnPage", required = false) String returnPage) {
		model.addAttribute("user", new User());

        if (returnPage != null)
            model.addAttribute("returnPage", returnPage);


        return "user/userRegistration";
    }

/*    @InitBinder
    public void initBinder(WebDataBinder binder) {
        StringTrimmerEditor stringtrimmer = new StringTrimmerEditor(true);
        binder.registerCustomEditor(String.class, stringtrimmer);
    }*/


    @RequestMapping(value = "/list")
    public String list(@RequestParam(name = "page", defaultValue = "1", required = false) int page,
                       @RequestParam(name = "size", required = false, defaultValue = "10") int size,
                       @RequestParam(name = "orderBy", required = false, defaultValue = "id") String orderBy,
                       @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort,
                       @RequestParam(name = "searchArguments", required = false, defaultValue = "&") String usedSearchArguments,
                       @ModelAttribute("user") User user,
                       Model model) {

        model.addAttribute("requestMapping", "list");

        PaginationDetails paginationDetails = PaginationDetails.builder().page(page).size(size).orderBy(orderBy).sort(sort).build();
        model.addAllAttributes(userSearchStrategy.prepareSearchForm(user, paginationDetails));

        return "/user/userList";
    }

	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable(value = "id") Long id, Model model) {
		Optional<User> user = userRepository.findById(id);

		if (user.isPresent())
			model.addAttribute("user", user.get());
		else
			throw new NoSuchElementException("User with id " + id + " not exists");

		return "user/registerUser";
	}

	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public String edit(Model model, Authentication authentication) {
		User user = userRepository.findByLogin(authentication.getName());
		model.addAttribute("user", user);

		return "user/registerUser";
	}

	@RequestMapping(value = "createUserByAdmin")
	public String createUserByAdmin(Model model) {
		model.addAttribute("user", new User());

		return "user/registerUser";
	}

	@RequestMapping(value = "register", method = RequestMethod.GET)
	public String register(Model model) {
		User user = new User();
		user.setActive(false);
		model.addAttribute("user", user);

		return "user/registerUser";
	}

	@RequestMapping(value = "confirmRegistration", method = RequestMethod.GET)
	public String confirmRegistration(@RequestParam("token") String token, Model model) {
		Result result = userService.activate(token);
		model.addAttribute("result", result.isSuccess());

		return "user/confirmRegistration";
	}

	@RequestMapping(value = "register", method = RequestMethod.POST)
	public ModelAndView register(@ModelAttribute("user") @Validated({User.ValidateAllFieldsWithoutPass.class,
			User.ValidatePassOnly.class}) User user, BindingResult bindingResult, HttpServletRequest request) {
		ModelAndView model = new ModelAndView("user/registerUser");

		if (bindingResult.hasErrors()) {
			return model;
		}

		Result result = userService.saveNewUser(user);

		if (result.isError()) {
			result.convertToMvcError(bindingResult);
		} else {
			if (!user.getActive()) {
				emailService.sendEmailWithAccountActivationLink(user, request.getRequestURL().toString().replace("user/register", ""));
				model.setViewName("redirect:/user/registrationSuccess?withoutActivation=true");
			} else
				model.setViewName("redirect:/user/registrationSuccess");
		}

		return model;
	}

	@RequestMapping(value = "adminSettings/{id}", method = RequestMethod.GET)
	public String adminSettings(@PathVariable("id") Long id, Model model) {
		Optional<User> user = userRepository.findById(id);

		if (user.isPresent())
			model.addAttribute("user", user);
		else
			throw new NoSuchElementException();
		
		return "user/userEdit";
		
	}

	@RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
	public ModelAndView delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		Optional<User> user = userRepository.findById(id);

		ModelAndView model = new ModelAndView("redirect:/user/list");
		Result<User> result = userService.deleteUser(user.get());

		result.ifError(e -> {
			e.getValidationResult().entrySet().stream().findAny().ifPresent(c -> {
				redirectAttributes.addFlashAttribute("message", c.getValue().getValidatorCode());
			});
			model.setViewName("redirect:/user/edit/" + user.get().getId());
		});

		return model;
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public ModelAndView update(@ModelAttribute("user") @Validated({User.ValidateAllFieldsWithoutPass.class}) User user, BindingResult bindingResult, Authentication authentication) {
		ModelAndView model = new ModelAndView("redirect:/");

		authentication.getAuthorities().stream().filter(e -> e.equals("ROLE_ADMIN")).findAny().ifPresent(e -> {
			model.setViewName("redirect:/user/list");
		});

		if (bindingResult.hasErrors())
			model.setViewName("user/registerUser");
		else {
			Result<User> result = userService.saveUser(user);
			result.ifError(() -> {
				result.convertToMvcError(bindingResult);
				model.setViewName("user/registerUser");
			});
		}

		return model;
	}

	private ModelAndView redirectDependsOnUsePlace(boolean passChangedFromAdministrationSite, long userId) {
		ModelAndView model = null;

		if (passChangedFromAdministrationSite)
			model = new ModelAndView("redirect:/user/edit/" + userId);
		else
			model = new ModelAndView("redirect:/user/account");

		return model;
	}

	@RequestMapping(value = "changePass", method = RequestMethod.POST)
	public ModelAndView changePass(@ModelAttribute("user") @Validated({User.ValidatePassOnly.class}) User user,
								   @RequestParam(value = "passChangedFromAdministrationSite", defaultValue = "false") boolean passChangedFromAdministrationSite, BindingResult bindingResult, HttpServletRequest request) {
		ModelAndView model = redirectDependsOnUsePlace(passChangedFromAdministrationSite, user.getId());

		if (bindingResult.hasErrors()) {
			model.setViewName("user/changePass");
			return model;
		} else {
			Result<User> result = userService.changePass(user);

			result.ifError(() -> {
				result.convertToMvcError(bindingResult);
				model.setViewName("user/changePass");
			});
		}

		return model;
	}

	@RequestMapping(value = "changePass/{id}", method = RequestMethod.GET)
	public String changePass(@PathVariable("id") Long id, Model model) {
		Optional<User> user = userRepository.findById(id);

		if (user.isEmpty())
			throw new NoSuchElementException();

		model.addAttribute("user", user.get());
		model.addAttribute("passChangedFromAdministrationSite", true);

		return "/user/changePass";
	}

	@RequestMapping(value = "changePass", method = RequestMethod.GET)
	public String changePass(Model model, Authentication authentication) {
		User user = userRepository.findByLogin(authentication.getName());

		model.addAttribute("user", user);

		return "/user/changePass";
	}

	@RequestMapping(value = "checkLoginAlreadyExists", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkLoginAlreadyExists(@RequestParam("login") String login) {
		return userRepository.countByLogin(login) > 0;
	}

	@RequestMapping(value = "checkEmailAlreadyExists", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkEmailAlreadyExists(@RequestParam("email") String login) {
		return userRepository.countByEmail(login) > 0;
	}

	@RequestMapping(value = "registrationSuccess")
	public String registrationSuccess(@RequestParam(value = "withoutActivation", required = false, defaultValue = "false") boolean withoutActivation, Model model) {
		model.addAttribute("withoutActivation", withoutActivation);

		return "user/registrationSuccess";
	}

	@RequestMapping(value = "login")
	public String login(Model model, @RequestParam(value = "error", required = false, defaultValue = "false") boolean error) {
		model.addAttribute("user", new User());
		model.addAttribute("error", error);

		return "user/userLogin";
	}

	@RequestMapping(value = "account")
	public ModelAndView userAccount() {

		return new ModelAndView("user/userHome");
	}

    @RequestMapping(value = "loadUserPhoneNumber", method = RequestMethod.GET)
    public @ResponseBody
    Integer loadUserPhoneNumber(@RequestParam("userId") long userId) {
        return userRepository.findPhoneNumberById(userId);
    }

}
