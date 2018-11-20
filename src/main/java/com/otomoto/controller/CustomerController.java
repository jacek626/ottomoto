package com.otomoto.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.otomoto.entity.Customer;
import com.otomoto.repository.CustomerRepository;

@Controller
@RequestMapping("customer/")
public class CustomerController {
	
	Logger logger = LoggerFactory.getLogger(CustomerController.class);
	
	@Autowired
	CustomerRepository customerRepository;
	
	@RequestMapping(value="registration", method = RequestMethod.GET)
	public String register(Model model) {
		model.addAttribute("customer", new Customer());	
		
		return "customer/customerRegistration";
	}
	
	@RequestMapping(value="registration", method = RequestMethod.POST)
	public String registerPost(@ModelAttribute("customer") @Valid Customer customer,
			 BindingResult bindingResult, Model model, Errors errors) {
		
		
		if (bindingResult.hasErrors()) {
			for (ObjectError objectError : bindingResult.getAllErrors()) {
				logger.info("error: " + objectError.getCode() + "," + objectError.getDefaultMessage() +
					", " + 	objectError.getObjectName());
			}
		} 
		else if(!customer.getPassword().equals(customer.getPasswordConfirm())) {
			model.addAttribute("passwordsAreNotSame",true);
		}
		else if (!customerRepository.findByLogin(customer.getLogin()).isEmpty()) {
			model.addAttribute("loginAlreadyExists", true);
		}
		else if (customerRepository.countByEmail(customer.getEmail()) > 0) {
			model.addAttribute("emailAlreadyExists", true);
		}
	    else if (!bindingResult.hasErrors()) {
	    	customerRepository.save(customer);
	    	
	    	// wyslanie powitalnego emaila
	    	
	    //	return "redirect:/";
	    	return "redirect:/customer/registrationSuccess";
	    }
		
		
		return "customer/registration";
		//return "customer/registrationSuccess";
		
	}
	
	@RequestMapping(value="registrationSuccess")
	public String registrationSuccess() {
		
		return "customer/customerRegistrationSuccess";
	}
	
	@RequestMapping(value="login")
	public String login() {
		
		return "customer/customerLogin";
	}
}
