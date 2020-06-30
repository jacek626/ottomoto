package com.app.controller;

import com.app.dto.UserDto;
import com.app.entity.User;
import com.app.repository.UserRepository;
import com.app.searchform.SearchStrategy;
import com.app.service.EmailService;
import com.app.service.UserService;
import com.app.utils.PaginationDetails;
import com.app.utils.Result;
import com.app.validator.groups.ValidateAllFieldsWithoutPass;
import com.app.validator.groups.ValidatePassOnly;
import org.modelmapper.ModelMapper;
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

	private final SearchStrategy<User, UserDto> userSearchStrategy;

    private final ModelMapper modelMapper;

	public UserController(SearchStrategy<User, UserDto> userSearchStrategy, UserRepository userRepository, EmailService emailService, UserService userService, ModelMapper modelMapper) {
		this.userSearchStrategy = userSearchStrategy;
		this.userRepository = userRepository;
		this.emailService = emailService;
		this.userService = userService;
		this.modelMapper = modelMapper;
	}

    private UserDto convertToDto(User user) {
        UserDto userDto = modelMapper.map(user, UserDto.class);

        return userDto;
    }

    private User convertToEntity(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);

        return user;
    }

    @RequestMapping(value = "/list")
    public String list(@RequestParam(name = "page", defaultValue = "1", required = false) int page,
					   @RequestParam(name = "size", required = false, defaultValue = "10") int size,
					   @RequestParam(name = "orderBy", required = false, defaultValue = "id") String orderBy,
					   @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort,
					   @RequestParam(name = "searchArguments", required = false, defaultValue = "&") String usedSearchArguments,
					   @ModelAttribute("user") UserDto userDto,
					   Model model) {

        model.addAttribute("requestMapping", "list");

        PaginationDetails paginationDetails = PaginationDetails.builder().page(page).size(size).orderBy(orderBy).sort(sort).build();
		model.addAllAttributes(userSearchStrategy.prepareSearchForm(convertToEntity(userDto), paginationDetails));

        return "user/userList";
    }

	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable(value = "id") Long id, Model model) {
		Optional<User> user = userRepository.findById(id);

        if (user.isPresent())
            model.addAttribute("user", convertToDto(user.get()));
        else
            throw new NoSuchElementException("User with id " + id + " not exists");

		return "user/registerUser";
	}

	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public String edit(Model model, Authentication authentication) {
        User user = userRepository.findByLogin(authentication.getName());
        model.addAttribute("user", convertToDto(user));

        return "user/registerUser";
    }

	@RequestMapping(value = "createUserByAdmin")
	public String createUserByAdmin(Model model) {
        model.addAttribute("user", convertToDto(new User()));

        return "user/registerUser";
    }

	@RequestMapping(value = "register", method = RequestMethod.GET)
	public String register(Model model) {
        User user = new User();
        user.setActive(false);
        model.addAttribute("user", convertToDto(user));

        return "user/registerUser";
    }

	@RequestMapping(value = "confirmRegistration", method = RequestMethod.GET)
	public String confirmRegistration(@RequestParam("token") String token, Model model) {
		Result result = userService.activate(token);
		model.addAttribute("result", result.isSuccess());

		return "user/confirmRegistration";
	}

	@RequestMapping(value = "register", method = RequestMethod.POST)
	public ModelAndView register(@ModelAttribute("user") @Validated({ValidateAllFieldsWithoutPass.class,
			ValidatePassOnly.class}) UserDto userDto, BindingResult bindingResult, HttpServletRequest request) {
		ModelAndView model = new ModelAndView("user/registerUser");

		if (bindingResult.hasErrors()) {
			return model;
		}

		User user = convertToEntity(userDto);
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
	public ModelAndView update(@ModelAttribute("user") @Validated({ValidateAllFieldsWithoutPass.class}) UserDto userDto, BindingResult bindingResult, Authentication authentication) {
		ModelAndView model = new ModelAndView("redirect:/");

		authentication.getAuthorities().stream().filter(e -> e.getAuthority().equals("ROLE_ADMIN")).findAny().ifPresent(e -> {
			model.setViewName("redirect:/user/list");
		});

		if (bindingResult.hasErrors())
			model.setViewName("user/registerUser");
		else {
			Result<User> result = userService.saveUser(convertToEntity(userDto));
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
    public ModelAndView changePass(@ModelAttribute("user") UserDto userDto,
                                   @RequestParam(value = "passChangedFromAdministrationSite", defaultValue = "false") boolean passChangedFromAdministrationSite, BindingResult bindingResult, HttpServletRequest request) {
        User user = convertToEntity(userDto);
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

        model.addAttribute("user", convertToDto(user.get()));
        model.addAttribute("passChangedFromAdministrationSite", true);

		return "user/changePass";
    }

	@RequestMapping(value = "changePass", method = RequestMethod.GET)
	public String changePass(Model model, Authentication authentication) {
        User user = userRepository.findByLogin(authentication.getName());

        model.addAttribute("user", convertToDto(user));

		return "user/changePass";
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
        model.addAttribute("user", convertToDto(new User()));
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
