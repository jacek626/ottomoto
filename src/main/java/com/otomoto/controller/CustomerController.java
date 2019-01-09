package com.otomoto.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.otomoto.entity.Role;
import com.otomoto.entity.User;
import com.otomoto.repository.RoleRepository;
import com.otomoto.repository.UserRepository;

@Controller
@RequestMapping("customer/")
public class CustomerController {
	
	Logger logger = LoggerFactory.getLogger(CustomerController.class);
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@RequestMapping(value="registration", method = RequestMethod.GET)
	public String register(Model model) {
		model.addAttribute("user", new User());	
		
		return "customer/customerRegistration";
	}
	
	@RequestMapping(value="registration", method = RequestMethod.POST)
	public String registerPost(@ModelAttribute("customer") @Valid User user,
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
	    	Role customerRole = roleRepository.findByName("customer");
	    	
	    	if(customerRole == null)
	    		throw new Exception("Role w systemie nie sa zdefiniowane");
	    	
	    	user.setRole(customerRole);
	    	user.setActive(1);
	    	user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
	    	userRepository.save(user);
	    	
	    	// wyslanie powitalnego emaila
	    	
	    //	return "redirect:/";
	    	return "redirect:/customer/registrationSuccess";
	    }
		
		
		return "customer/customerRegistration";
		//return "customer/registrationSuccess";
		
	}
	
	@RequestMapping(value="registrationSuccess")
	public String registrationSuccess() {
		
		return "customer/customerRegistrationSuccess";
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
		
		
		return "customer/customerLogin";
	}
/*	
	@RequestMapping(value="login", method = RequestMethod.POST)
	public String loginPost(@ModelAttribute User user) {
		System.out.println("DDDDDDDDDDD");
		
		
		return "redirect:/";
	}*/
	
	@RequestMapping(value = "account")
	public String customerAccount(Model model) {
		
		return "customer/customerAccount";
	}

	public BCryptPasswordEncoder getbCryptPasswordEncoder() {
		return bCryptPasswordEncoder;
	}

	public void setbCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}
}
