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
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.otomoto.entity.Admin;
import com.otomoto.repository.AdminRepository;

@Controller
@RequestMapping("admin/")
public class AdminController {

	Logger logger = LoggerFactory.getLogger(AdminController.class);

	@Autowired
	private AdminRepository adminRepository;

	@RequestMapping(value = "register", method = RequestMethod.GET)
	public String register(Model model) {
		model.addAttribute("admin", new Admin());

		return "admin/register";
	}

	@RequestMapping(value = "register", method = RequestMethod.POST)
	public String  registerPost(@ModelAttribute("admin") @Valid Admin admin, BindingResult bindingResult, Model model, Errors errors) {

		if (bindingResult.hasErrors()) {
			for (ObjectError objectError : bindingResult.getAllErrors()) {
				logger.info("error: ", objectError.getCode(), objectError.getDefaultMessage(),
						objectError.getObjectName());
			}
		} 
		else if(!admin.getPassword().equals(admin.getPasswordConfirm())) {
			model.addAttribute("passwordsAreNotSame",true);
		}
		else if (!adminRepository.findByLogin(admin.getLogin()).isEmpty()) {
			model.addAttribute("loginAlreadyExists", true);
		}
		else if (!adminRepository.countByEmail(admin.getEmail()).isEmpty()) {
			model.addAttribute("emailAlreadyExists", true);
		}
	    else if (!bindingResult.hasErrors()) {
	    	adminRepository.save(admin);
	    	return "redirect:/";
	    }

		//return "admin/register";
		//return 	modelAndView.setViewName("register");
	//	modelAndView.setViewName("admin/register");
	//	modelAndView.setViewName("redirect:/");
		return "admin/register";  
	}
}
