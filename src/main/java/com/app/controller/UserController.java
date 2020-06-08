package com.app.controller;

import com.app.entity.Role;
import com.app.entity.User;
import com.app.repository.RoleRepository;
import com.app.repository.UserRepository;
import com.app.searchform.SearchStrategy;
import com.app.service.EmailService;
import com.app.service.UserService;
import com.app.utils.PaginationDetails;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
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

	//private BCryptPasswordEncoder bCryptPasswordEncoder;

	private final UserService userService;

    private final SearchStrategy<User> userSearchStrategy;

    private final int[] PAGE_SIZES = {5, 10, 20};

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

		return "user/userEdit";

	}

	@RequestMapping(value = "createUserByAdmin")
	public String createUserByAdmin(Model model) {
		User user = new User();
		user.setActive(true);
		
		model.addAttribute("user", user);
		
		return "user/userEdit";
	}
	
	@RequestMapping(value="register", method = RequestMethod.GET)
	public String register(Model model) {
		User user = new User();
		user.setActive(true);
		
		model.addAttribute("user", user);
		
		return "user/registerUser";
	}

	@RequestMapping(value = "register", method = RequestMethod.POST)
	public ModelAndView register(@ModelAttribute("user") @Validated({User.ValidateAllFieldsWithoutPass.class,
			User.ValidatePassOnly.class}) User user, BindingResult bindingResult) {
		ModelAndView model = new ModelAndView("redirect:/user/registrationSuccess");

		if (bindingResult.hasErrors()) {
			prepareModelToErrorDisplay(user, bindingResult, model);
			return model;
		}

		userService.saveNewUser(user).ifError(result -> {
			model.setViewName("user/registerUser");
			prepareModelToErrorDisplay(user, bindingResult, model);
		});

		return model;

	}

	private void prepareModelToErrorDisplay(@Validated({User.ValidateAllFieldsWithoutPass.class,
			User.ValidatePassOnly.class}) @ModelAttribute("user") User user, BindingResult bindingResult, ModelAndView model) {
		model.addObject("user", user);
		model.addObject("error", bindingResult);
		model.setViewName("user/registerUser");
	}

	@RequestMapping(value = "adminSettings/{id}", method = RequestMethod.GET)
	public String adminSettings(@PathVariable("id") Long id, Model model) {
		//public String edit(@PathVariable("id") Long id, BindingResult bindingResult, Model model, Errors errors) throws Exception { 
		Optional<User> user = userRepository.findById(id);

		if (user.isPresent())
			model.addAttribute("user", user);
		else
			throw new NoSuchElementException();
		
		return "user/userEdit";
		
	}
	
	@RequestMapping(value="delete/{id}", method = RequestMethod.GET)
	public String delete(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        //public String edit(@PathVariable("id") Long id, BindingResult bindingResult, Model model, Errors errors) throws Exception {
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
	
	@RequestMapping(value="update", method = RequestMethod.POST)
	public String update(@ModelAttribute("user") @Validated({User.ValidateAllFieldsWithoutPass.class}) User user, BindingResult bindingResult, Model model, Errors errors) {

        if (errors.hasErrors() || validateEmailBeforeUpdate(user, model)) {
            model.addAttribute("user", user);
            model.addAttribute("error", errors);
            return "user/userEdit";
        } else {
            Optional<User> userFromDB = userRepository.findById(user.getId());

            if (userFromDB.isPresent()) {
                user.setPassword(userFromDB.get().getPassword());
                userRepository.save(user);
			}
			else
				throw new NoSuchElementException ();
		
			return "redirect:/user/list";
		}
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
	
	
	// @Validated(User.ValidateAllFieldsWithoutPass.class) 
	@RequestMapping(value="insert", method = RequestMethod.POST)
	public String insert(@ModelAttribute("user") @Validated({User.ValidateAllFieldsWithoutPass.class, User.ValidatePassOnly.class})  User user, BindingResult bindingResult, Model model, Errors errors) {
	//	boolean userIsValid = true;
		
/*		if(user.getId() != null) {
			Optional<User> userFromDb = userRepository.findById(user.getId());
			
			if(!userFromDb.isPresent())
				throw new RuntimeException("brak usera o podanym id");
			
			userIsValid = validateUser(user, model, bindingResult);
			user.setPassword(userFromDb.get().getPassword());
			user.setLogin(userFromDb.get().getLogin());
		}
		else {
			userIsValid = validateUser(user, model, bindingResult);
			System.out.println(userIsValid);
			
		}*/
		
/*		
		if (userRepository.findByLogin(user.getLogin()).getId() != user.getId()) {
			model.addAttribute("loginAlreadyExists", true);
			errors.rejectValue("email", "loginAlreadyExists");
		}*/
		 if (userRepository.countByEmail(user.getEmail()) > 1) {
			errors.rejectValue("email", "emailAlreadyExists");
		}
		
	//	if(errors.hasErrors() || !userIsValid) {
		if(errors.hasErrors()) {
			model.addAttribute("user", user);
			model.addAttribute("error", errors);
			return "user/userEdit";
		} else {
			userRepository.save(user);

			return "redirect:/user/list";
		}
	}

	@RequestMapping(value = "checkLoginAlreadyExists", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkLoginAlreadyExists(@RequestParam("login") String login) {
		return userRepository.countByLogin(login) > 0;
	}

	@RequestMapping(value = "checkEmailAlreadyExists", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkEmailAlreadyExists(@RequestParam("email") String login, @RequestParam(name = "id", required = false) Long id) {
		if (id == null)
			return userRepository.countByEmail(login) > 0;
		else
			return userRepository.countByEmailAndIdNot(login, id) > 0;
	}

	@RequestMapping(value = "registration", method = RequestMethod.POST)
	public String registerPost(@ModelAttribute("user") @Valid User user, @RequestParam String returnPage,
							   BindingResult bindingResult, Model model, Errors errors) throws Exception {
		
		
		if (bindingResult.hasErrors()) {
			for (ObjectError objectError : bindingResult.getAllErrors()) {
				logger.error(objectError.toString());
			}
		} 
		else if(!user.getPassword().equals(user.getPasswordConfirm())) {
			model.addAttribute("passwordsAreNotSame",true);
		}
		else if (userRepository.findByLogin(user.getLogin()) != null) {
			//	model.addAttribute("loginAlreadyExists", true);
			bindingResult.addError(new ObjectError("login", "loginAlreadyExists"));
		}
		else if (userRepository.countByEmail(user.getEmail()) > 0) {
			model.addAttribute("emailAlreadyExists", true);
		}
	    else if (!bindingResult.hasErrors()) {
	    	Role userRole = roleRepository.findByName("ROLE_USER");
	    	
	    	if(userRole == null)
	    		throw new NoSuchElementException("No defined roles in the system");
	    	
	    	user.setRole(userRole);
	    	user.setActive(true);
	    	//user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
	    	// do zmiany
	    	userRepository.save(user);
	    	
	    	// wyslanie powitalnego emaila
	    	
	    	
	    	if(Strings.isNotBlank(returnPage))
	    		return "redirect:/user/"+returnPage;
	    	
	    	return "redirect:/user/registrationSuccess";
	    }
		
		
		if(Strings.isNotBlank(returnPage))
			model.addAttribute("returnPage",returnPage);
		
		return "user/userRegistration";
		//return "user/registrationSuccess";
		
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
