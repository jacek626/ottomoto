package com.app.controller;

import com.app.entity.User;
import com.app.repository.RoleRepository;
import com.app.repository.UserRepository;
import com.app.searchform.SearchStrategy;
import com.app.service.EmailService;
import com.app.service.UserService;
import com.app.utils.PaginationDetails;
import com.app.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Controller
@RequestMapping("user/")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	final
	UserRepository userRepository;

	final
	RoleRepository roleRepository;

	//@Autowired
	//ApplicationEventPublisher applicationEventPublisher;

	private final EmailService emailService;

	private final UserService userService;

    private final SearchStrategy<User> userSearchStrategy;

	public UserController(SearchStrategy<User> userSearchStrategy, UserRepository userRepository, RoleRepository roleRepository, EmailService emailService, UserService userService) {
		this.userSearchStrategy = userSearchStrategy;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
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

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        StringTrimmerEditor stringtrimmer = new StringTrimmerEditor(true);
        binder.registerCustomEditor(String.class, stringtrimmer);
    }


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

		return "user/userEdit";

	}

	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public String edit(Model model, Authentication authentication) {
		User user = userRepository.findByLogin(authentication.getName());

		model.addAttribute("user", user);

		return "user/registerUser";

	}

	@RequestMapping(value = "createUserByAdmin")
	public String createUserByAdmin(Model model) {
		User user = new User();
		user.setActive(true);
		
		model.addAttribute("user", user);
		
		return "user/userEdit";
	}

	@RequestMapping(value = "register", method = RequestMethod.GET)
	public String register(Model model) {
		User user = new User();
		user.setActive(true);

		model.addAttribute("user", user);

		return "user/registerUser";
	}

	@RequestMapping(value = "confirmRegistration", method = RequestMethod.GET)
	public String confirmRegistration(@PathVariable("token") String token, Model model) {
		userService.activate(token);

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
		List<FieldError> fieldErrorS = null;

		if (result.isError()) {
			result.convertToMvcError(bindingResult);
		} else {
			emailService.sendEmailWithAccountActivationLink(user, request.getRequestURL().toString().replace("user/register", ""));
			model.setViewName("redirect:/user/registrationSuccess");
		}

		return model;
	}

	private void prepareModelToErrorDisplay(@Validated({User.ValidateAllFieldsWithoutPass.class,
			User.ValidatePassOnly.class}) @ModelAttribute("user") User user, BindingResult bindingResult, ModelAndView model) {
		model.setViewName("user/registerUser");
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
	
	@RequestMapping(value="delete/{id}", method = RequestMethod.GET)
	public String delete(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent() && user.get().getAnnouncementList().size() > 0) {
            redirectAttributes.addFlashAttribute("message", "user ma przypisane aukcje, zanim usuniesz usuera trzeba je skasowac");

            return "redirect:/user/edit/" + user.get().getId();
        } else {
            redirectAttributes.addFlashAttribute("message", "konto usuniete");
            userRepository.delete(user.get());

            return "redirect:/user/list";
        }
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
			result.ifError(r -> {
				result.convertToMvcError(bindingResult);

				model.setViewName("user/registerUser");
			});
		}

		return model;
	}
	
	@RequestMapping(value="changePass", method = RequestMethod.POST)
	public String changePass(@ModelAttribute("user") @Validated({User.ValidatePassOnly.class}) User user, BindingResult bindingResult, Model model, Errors errors) {

        if (errors.hasErrors() || validateEmailBeforeUpdate(user, model)) {
            model.addAttribute("user", user);
            model.addAttribute("error", errors);
            return "user/userChangePass";
        } else {
            Optional<User> userFromDB = userRepository.findById(user.getId());

            if (userFromDB.isPresent()) {
                //		userFromDB.get().setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
				// do zmiany
				userRepository.save(userFromDB.get());
			} else
				throw new NoSuchElementException();

			return "redirect:/user/edit/" + user.getId();
		}
	}

	@RequestMapping(value = "changePass/{id}", method = RequestMethod.GET)
	public String changePass(@PathVariable("id") Long id, Model model) {
		Optional<User> user = userRepository.findById(id);

		if (user.isEmpty())
			throw new NoSuchElementException();

		model.addAttribute("user", user.get());

		return "/user/userChangePass";
	}

	@RequestMapping(value = "changePass", method = RequestMethod.GET)
	public String changePass(Model model, Authentication authentication) {
		User user = userRepository.findByLogin(authentication.getName());

		model.addAttribute("user", user);

		return "/user/userChangePass";
	}

	private boolean validateEmailBeforeUpdate(User user, Model model) {
		if (userRepository.countByEmailAndIdNot(user.getEmail(), user.getId()) > 0) {
			model.addAttribute("emailAlreadyExists", true);
			return true;
		}
		return false;
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

	@RequestMapping(value="registrationSuccess")
	public String registrationSuccess() {
		
		return "user/registrationSuccess";
	}
	
	@RequestMapping(value="login", method = RequestMethod.GET)
	public String login(Model model, @RequestParam(value = "error", required = false) String error) {
		model.addAttribute("user",new User());
		
		String errorMessge = null;
        if(error != null) {
            errorMessge = "username or password are incorrect";
        }
    //    if(logout != null) {
     //       errorMessge = "You have been successfully logged out !!";
  //      }
		
        model.addAttribute("errorMessge", errorMessge);
//        model.addAttribute("errorMessge", errorMessge);
		
		return "user/userLogin";
	}
/*	
	@RequestMapping(value="login", method = RequestMethod.POST)
	public String loginPost(@ModelAttribute User user) {
		System.out.println("DDDDDDDDDDD");
		
		
		return "redirect:/";
	}*/
	
	@RequestMapping(value = "account")
	public String userAccount(Model model) {

        return "user/userHome";
    }

    @RequestMapping(value = "myAnnouncements")
    public String myAnnouncements(Model model) {

        return "redirect:/announcement/list";
    }

    @RequestMapping(value = "loadUserPhoneNumber", method = RequestMethod.GET)
    public @ResponseBody
    Integer loadUserPhoneNumber(@RequestParam("userId") long userId) {
        return userRepository.findPhoneNumberById(userId);
    }

}
