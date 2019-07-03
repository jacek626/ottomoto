package com.otomoto.controller;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.otomoto.entity.QUser;
import com.otomoto.entity.Role;
import com.otomoto.entity.User;
import com.otomoto.repository.RoleRepository;
import com.otomoto.repository.UserRepository;
import com.querydsl.core.BooleanBuilder;



@Controller
@RequestMapping("user/")
public class UserController {
	
	Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	ApplicationEventPublisher applicationEventPublisher;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
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
	public String list(@RequestParam(name = "page", required = false) Optional<Integer> page,
			@RequestParam(name = "size", required = false, defaultValue = "10") Optional<Integer> size,
			@RequestParam(name = "orderBy", required = false, defaultValue = "id") Optional<String> orderBy,
			@RequestParam(name = "sort", required = false, defaultValue = "ASC") Optional<String> sort,
			@RequestParam(name = "searchArguments",required = false, defaultValue = "&") String searchArguments, 
			@ModelAttribute("user")  User user,
			Model model) {
		
		user.prepareFiledsForSearch();
		
		BooleanBuilder queryBuilder = new BooleanBuilder();
		searchArguments = prepareQueryAndSearchArguments(user, searchArguments, queryBuilder);
		
		PageRequest pageable = null;
		pageable = PageRequest.of(page.orElse(1) - 1, size.orElse(10), Direction.fromString(sort.orElse("ASC")), orderBy.orElse("id"));
	
		Page<User> userPage = userRepository.findAll(queryBuilder, pageable);
		
        if(userPage.getTotalPages() > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1 ,userPage.getTotalPages()).boxed().collect(Collectors.toList());
            model.addAttribute("numbers", pageNumbers);
        }
        
        
        model.addAttribute("pages", userPage);
        model.addAttribute("orderBy", orderBy.orElse(""));
        model.addAttribute("sort", sort.orElse("ASC"));
        model.addAttribute("searchArguments", searchArguments);
        model.addAttribute("page", page.orElse(1));
        model.addAttribute("size", size.orElse(10));
        model.addAttribute("pageSizes", PAGE_SIZES);
        model.addAttribute("user", user);
		
		return "/user/userList";
	}
	
	private String prepareQueryAndSearchArguments(User user, String searchArguments, BooleanBuilder queryBuilder) {
		StringBuilder stringBuilder = new StringBuilder(searchArguments);
		
		if(StringUtils.isNotBlank(user.getLogin())) {
			queryBuilder.and(QUser.user.login.contains(user.getLogin()));
			stringBuilder.append("login=" + user.getLogin() + "&");
		}
		if(StringUtils.isNotBlank(user.getEmail())) {
			queryBuilder.and(QUser.user.email.contains(user.getEmail()));
			stringBuilder.append("email=" + user.getEmail() + "&");
		}
		if(StringUtils.isNotBlank(user.getFirstName())) {
			queryBuilder.and(QUser.user.firstName.contains(user.getFirstName()));
			stringBuilder.append("firstName=" + user.getFirstName() + "&");
		}
		if(StringUtils.isNotBlank(user.getLastName())) {
			queryBuilder.and(QUser.user.lastName.contains(user.getLastName()));
			stringBuilder.append("lastName=" + user.getLastName() + "&");
		}
		if(user.getActive() != null) {
			queryBuilder.and(QUser.user.active.eq(user.getActive()));
			stringBuilder.append("active=" + user.getActive() + "&");
		}
		if(StringUtils.isNotBlank(user.getCity())) {
			queryBuilder.and(QUser.user.city.contains(user.getCity()));
			stringBuilder.append("city=" + user.getCity() + "&");
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
			throw new RuntimeException("brak usera o podanym id");
		
		return "user/userEdit";
		
	}
	
	@RequestMapping(value="createUserByAdmin")
	public String createUserByAdmin(Model model) {
		User user = new User();
		user.setActive(true);
		
		model.addAttribute("user", user);
		
		return "user/userEdit";
	}
	
	@RequestMapping(value="adminSettings/{id}", method = RequestMethod.GET)
	public String adminSettings(@PathVariable("id") Long id, Model model) {
		//public String edit(@PathVariable("id") Long id, BindingResult bindingResult, Model model, Errors errors) throws Exception { 
		Optional<User> user = userRepository.findById(id);
		
		if(user.isPresent()) 
			model.addAttribute("user", user);
		else
			throw new RuntimeException("brak admina o podanym id");
		
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
	
	private boolean validateUser(User user, Model model, BindingResult bindingResult) {
		boolean userIsValid = true;
		
		if (bindingResult.hasErrors()) {
			for (ObjectError objectError : bindingResult.getAllErrors()) {
				logger.error(objectError.toString());
			}
			userIsValid = false;
		} 
		 if(user.getId() == null && StringUtils.isBlank(user.getPasswordConfirm())) {
			model.addAttribute("validationFieldIsEmpty",true);
			userIsValid = false;
		}
		else if((Objects.nonNull(user.getPassword())  && Objects.nonNull(user.getPasswordConfirm())) && !user.getPassword().equals(user.getPasswordConfirm())) {
			model.addAttribute("passwordsAreNotSame",true);
			userIsValid = false;
		}
		else if (user.getId() == null && StringUtils.isNotBlank(user.getLogin()) && userRepository.findByLogin(user.getLogin()) != null) {
			model.addAttribute("loginAlreadyExists", true);
			userIsValid = false;
		}
		else if (user.getId() == null && StringUtils.isNotBlank(user.getEmail()) && userRepository.countByEmail(user.getEmail()) > 0) {
			model.addAttribute("emailAlreadyExists", true);
			userIsValid = false;
		}
		
		return userIsValid;
	}
	
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
				throw new RuntimeException("brak usera o podanym id");
		
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
				userFromDB.get().setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
				userRepository.save(userFromDB.get());
			}
			else
				throw new RuntimeException("brak usera o podanym id");
			
			return "redirect:/user/edit/" + user.getId();
		}
	}
	
	@RequestMapping(value="changePass/{id}")
   public String changePass(@PathVariable("id") Long id, Model model) {
			Optional<User> user = userRepository.findById(id);
			
			if(!user.isPresent())
				throw new RuntimeException("brak usera o podanym id");
		
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
		boolean userIsValid = true;
		
		if(user.getId() != null) {
		//	updateUser(user);
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
			
		}
		
/*		
		if (userRepository.findByLogin(user.getLogin()).getId() != user.getId()) {
			model.addAttribute("loginAlreadyExists", true);
			errors.rejectValue("email", "loginAlreadyExists");
		}*/
		 if (userRepository.countByEmail(user.getEmail()) > 1) {
			errors.rejectValue("email", "emailAlreadyExists");
		}
		
		if(errors.hasErrors() || !userIsValid) {
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
		return userRepository.countByLogin(login) > 0 ? true : false; 
	}
	
	@RequestMapping(value="checkEmailAlreadyExists",method=RequestMethod.POST)
	public @ResponseBody boolean checkEmailAlreadyExists(@RequestParam("email") String login,@RequestParam(name="id", required=false) Long id) {
		if(id == null)
			return userRepository.countByEmail(login) > 0 ? true : false; 
		else
			return userRepository.countByEmailAndIdNot(login, id) > 0 ? true : false; 
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
	    		throw new Exception("Role w systemie nie sa zdefiniowane");
	    	
	    	user.setRole(userRole);
	    	user.setActive(true);
	    	user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
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
		
		return "user/userRegistrationSuccess";
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
		
		return "user/userAccountDetails";
	}

	public BCryptPasswordEncoder getbCryptPasswordEncoder() {
		return bCryptPasswordEncoder;
	}

	public void setbCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}
}
