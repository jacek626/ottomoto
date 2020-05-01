package com.app.controller;

import com.app.entity.QUser;
import com.app.entity.Role;
import com.app.entity.User;
import com.app.repository.RoleRepository;
import com.app.repository.UserRepository;
import com.app.service.UserService;
import com.app.utils.Result;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;



@Controller
@RequestMapping("user/")
public class UserController {
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	//@Autowired
	//ApplicationEventPublisher applicationEventPublisher;
	
	//@Autowired
	//private EmailService emailService;
	
	//private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserService userService;
	
	private final int[] PAGE_SIZES = {5,10,20};
	
	@RequestMapping(value="registration/{returnPage}", method = RequestMethod.GET)
	public String register(Model model, @PathVariable(name = "returnPage", required = false)  Optional<String> returnPage) {
		model.addAttribute("user", new User());	
		
		if(returnPage.isPresent())
			model.addAttribute("returnPage", returnPage.get());
		
		return "user/userRegistration";
	}
	
	@InitBinder
	public void initBinder ( WebDataBinder binder )
	{
	    StringTrimmerEditor stringtrimmer = new StringTrimmerEditor(true);  
	    binder.registerCustomEditor(String.class, stringtrimmer);
	}
	
	
	@RequestMapping(value="/list")
	public String list(@RequestParam(name = "page", defaultValue = "1", required = false) int page,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "orderBy", required = false, defaultValue = "id") String orderBy,
			@RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort,
			@RequestParam(name = "searchArguments",required = false, defaultValue = "&") String searchArguments, 
			@ModelAttribute("user")  User user,
			Model model) {
		
		user.prepareFiledsForSearch();
		
		BooleanBuilder queryBuilder = new BooleanBuilder();
		searchArguments = prepareQueryAndSearchArguments(user, searchArguments, queryBuilder);
		
		PageRequest pageable = null;
		pageable = PageRequest.of(page, size, Direction.fromString(sort), orderBy);
	
		Page<User> userPage = userRepository.findAll(queryBuilder, pageable);
		
        if(userPage.getTotalPages() > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1 ,userPage.getTotalPages()).boxed().collect(Collectors.toList());
            model.addAttribute("numbers", pageNumbers);
        }
        
        
        model.addAttribute("pages", userPage);
        model.addAttribute("orderBy", orderBy);
        model.addAttribute("sort", sort);
        model.addAttribute("searchArguments", searchArguments);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("pageSizes", PAGE_SIZES);
        model.addAttribute("user", user);
		
		return "/user/userList";
	}
	
	private String prepareQueryAndSearchArguments(User user, String searchArguments, BooleanBuilder queryBuilder) {
		StringBuilder stringBuilder = new StringBuilder(searchArguments);
		
		if(StringUtils.isNotBlank(user.getLogin())) {
			queryBuilder.and(QUser.user.login.contains(user.getLogin()));
			stringBuilder.append("login=").append(user.getLogin()).append("&");
		}
		if(StringUtils.isNotBlank(user.getEmail())) {
			queryBuilder.and(QUser.user.email.contains(user.getEmail()));
			stringBuilder.append("email=").append(user.getEmail()).append("&");
		}
		if(StringUtils.isNotBlank(user.getFirstName())) {
			queryBuilder.and(QUser.user.firstName.contains(user.getFirstName()));
			stringBuilder.append("firstName=").append(user.getFirstName()).append("&");
		}
		if(StringUtils.isNotBlank(user.getLastName())) {
			queryBuilder.and(QUser.user.lastName.contains(user.getLastName()));
			stringBuilder.append("lastName=").append(user.getLastName()).append("&");
		}
		if(user.getActive() != null) {
			queryBuilder.and(QUser.user.active.eq(user.getActive()));
			stringBuilder.append("active=").append(user.getActive()).append("&");
		}
		if(StringUtils.isNotBlank(user.getCity())) {
			queryBuilder.and(QUser.user.city.contains(user.getCity()));
			stringBuilder.append("city=").append(user.getCity()).append("&");
		}
		
		return stringBuilder.toString();
	}
	
	
	
	@RequestMapping(value="edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") Long id, Model model) {
	//public String edit(@PathVariable("id") Long id, BindingResult bindingResult, Model model, Errors errors) throws Exception { 
		Optional<User> user = userRepository.findById(id);
		
		if(user.isPresent()) 
			model.addAttribute("user", user.get());
		else
			throw new NoSuchElementException("User with id "+ id +" not exists");
		
		return "user/userEdit";
		
	}
	
	@RequestMapping(value="createUserByAdmin")
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
	
	@RequestMapping(value="register", method = RequestMethod.POST)
	public String register(@ModelAttribute("user") @Validated({ User.ValidateAllFieldsWithoutPass.class,
			User.ValidatePassOnly.class }) User user, BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("user", user);
			model.addAttribute("error", bindingResult);
			return "user/register";
		}

		if (user.getActive() == null)
			user.setActive(true);
		
		LocaleContextHolder.getLocale();
		System.out.println(LocaleContextHolder.getLocale());

		Result result = userService.saveUser(user);

		if (result.getValidationResult().isEmpty()) {
			

			userService.sentEmailWithAccountActivationLink(user); 
			
	//		emailService.sendEmailFromSystem(senderEmailAddress, emailText, subject, receiverEmailAddress);
			
			return "redirect:/user/registrationSuccess";
		}
		else {
		//	result.getValidationResult().entrySet().stream().forEach(e -> bindingResult.reject(e.getKey(), e.getValue()));
		//	result.getValidationResult().entrySet().stream().peek(e -> System.out.println("error, key " + e.getKey() + " value " + e.getValue()));
			
		//	if(errors.getAllErrors() != null)
		//		errors.getAllErrors().addAll(result.getValidationResult().entrySet().stream().map(e ->  new ObjectError(e.getKey(), e.getValue())).collect(Collectors.toList()));



		//	for (Entry<String, ValidationDetails> entry : result.getValidationResult().entrySet()) {

				// o to co trzeba poprawic po zmiane na ResultDetails  przestalo dzialac
				//bindingResult.addError(new FieldError("user", entry.getKey(), "", false, null, null, entry.getValue().name()));
			}
			
			model.addAttribute("user", user);
			model.addAttribute("error", bindingResult);
			
			return "user/registerUser";

	}
	
	@RequestMapping(value="adminSettings/{id}", method = RequestMethod.GET)
	public String adminSettings(@PathVariable("id") Long id, Model model) {
		//public String edit(@PathVariable("id") Long id, BindingResult bindingResult, Model model, Errors errors) throws Exception { 
		Optional<User> user = userRepository.findById(id);
		
		if(user.isPresent()) 
			model.addAttribute("user", user);
		else
			throw new NoSuchElementException ();
		
		return "user/userEdit";
		
	}
	
	@RequestMapping(value="delete/{id}", method = RequestMethod.GET)
	public String delete(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
		//public String edit(@PathVariable("id") Long id, BindingResult bindingResult, Model model, Errors errors) throws Exception { 
		Optional<User> user = userRepository.findById(id);
		
		if(user.get().getAnnouncementList().size() > 0) {
			redirectAttributes.addFlashAttribute("message", "user ma przypisane aukcje, zanim usuniesz usuera trzeba je skasowac");
			
			return "redirect:/user/edit/" + user.get().getId();
		}
		else {
			redirectAttributes.addFlashAttribute("message", "konto usuniete");
		 	userRepository.delete(user.get());
			
			return "redirect:/user/list";
		}
	}
	
/*	private Model createUser(User user, Model model, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			for (ObjectError objectError : bindingResult.getAllErrors()) {
				logger.error(objectError.toString());
			}
		} 
		else if(!user.getPassword().equals(user.getPasswordConfirm())) {
			model.addAttribute("passwordsAreNotSame",true);
		}
		else if (userRepository.findByLogin(user.getLogin()) != null) {
			model.addAttribute("loginAlreadyExists", true);
		}
		else if (userRepository.countByEmail(user.getEmail()) > 0) {
			model.addAttribute("emailAlreadyExists", true);
		}
	    else if (!bindingResult.hasErrors()) {
	    	Role userRole = roleRepository.findByName("ROLE_USER");
	    	
	    	if(userRole == null)
	    		throw new Exception("Role w systemie nie sa zdefiniowane");
	    	
	    	user.setRole(userRole);
	    	user.setActive(true);
	    	user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
	    	userRepository.save(user);
	    	
	    	model.addAttribute("user", user);
	    }
		
	    	return model;
	}*/
	
	@RequestMapping(value="update", method = RequestMethod.POST)
	public String update(@ModelAttribute("user") @Validated({User.ValidateAllFieldsWithoutPass.class}) User user, BindingResult bindingResult, Model model, Errors errors) {
		
		if(errors.hasErrors() || !validateEmailBeforeUpdate(user, model)) {
			model.addAttribute("user", user);
			model.addAttribute("error",errors);
			return "user/userEdit";
		}
		else {
			Optional<User> userFromDB = userRepository.findById(user.getId());
			
			if(userFromDB.isPresent()) {
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
		
		if(errors.hasErrors() || !validateEmailBeforeUpdate(user, model)) {
			model.addAttribute("user", user);
			model.addAttribute("error",errors);
			return "user/userChangePass";
		}
		else {
			Optional<User> userFromDB = userRepository.findById(user.getId());
			
			if(userFromDB.isPresent()) {
		//		userFromDB.get().setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
				// do zmiany
				userRepository.save(userFromDB.get());
			}
			else
				throw new NoSuchElementException();
			
			return "redirect:/user/edit/" + user.getId();
		}
	}
	
	@RequestMapping(value="changePass/{id}")
   public String changePass(@PathVariable("id") Long id, Model model) {
			Optional<User> user = userRepository.findById(id);
			
			if(user.isEmpty())
				throw new NoSuchElementException();
		
			model.addAttribute("user", user.get());
			
			return "/user/userChangePass";
	}
	
	private boolean validateEmailBeforeUpdate(User user, Model model) {
		if(userRepository.countByEmailAndIdNot(user.getEmail(), user.getId()) > 0) {
			model.addAttribute("emailAlreadyExists", true);
			return false;
		}
			return true;
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
			model.addAttribute("error",errors);
			return "user/userEdit";
		}
		else {
			userRepository.save(user);
		
			return "redirect:/user/list";
		}
	}
	
	@RequestMapping(value="checkLoginAlreadyExists",method=RequestMethod.POST)
	public @ResponseBody boolean checkLoginAlreadyExists(@RequestParam("login") String login) {
		return userRepository.countByLogin(login) > 0;
	}
	
	@RequestMapping(value="checkEmailAlreadyExists",method=RequestMethod.POST)
	public @ResponseBody boolean checkEmailAlreadyExists(@RequestParam("email") String login,@RequestParam(name="id", required=false) Long id) {
		if(id == null)
			return userRepository.countByEmail(login) > 0;
		else
			return userRepository.countByEmailAndIdNot(login, id) > 0;
	}
	
	@RequestMapping(value="registration", method = RequestMethod.POST)
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
			model.addAttribute("loginAlreadyExists", true);
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

}
