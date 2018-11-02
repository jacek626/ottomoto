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

import com.otomoto.AdminRepository;
import com.otomoto.entity.Admin;

@Controller
@RequestMapping("admin/")
public class AdminController {
	
	Logger logger = LoggerFactory.getLogger(AdminController.class);
	
	@Autowired
	private AdminRepository adminRepository;
	
	@RequestMapping(value="register", method=RequestMethod.GET)
	public String register(Model model) {
		model.addAttribute("admin", new Admin());
		
		return "admin/register";
	}
	
	
	@RequestMapping(value="register", method=RequestMethod.POST)
	public String registerPost(@ModelAttribute("admin") @Valid Admin admin,
			 BindingResult result, WebRequest request, Errors errors) {
		
		
		if(!result.hasErrors())
			adminRepository.save(admin);
		
		for (ObjectError objectError : result.getAllErrors()) {
			logger.info("error: ",objectError.getCode(), objectError.getDefaultMessage(), objectError.getObjectName());
		}
		
		return "admin/register";
		 //return new ModelAndView("admin/register", "user", admin);
	}
}
