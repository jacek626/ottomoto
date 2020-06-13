package com.app.controller;

import com.app.entity.User;
import com.app.repository.UserRepository;
import com.app.utils.SystemEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
@RequestMapping("system/")
public class SystemSettingsController {

	private static final Logger logger = LoggerFactory.getLogger(SystemSettingsController.class);

	private final UserRepository userRepository;

	private final SystemEmail systemEmail;

	public SystemSettingsController(UserRepository userRepository, SystemEmail systemEmail) {
		this.userRepository = userRepository;
		this.systemEmail = systemEmail;
	}

	@RequestMapping(value = "register", method = RequestMethod.GET)
	public String register(Model model) {
		model.addAttribute("user", new User());

		return "adminsitration/register";
	}

	@RequestMapping(value = "register", method = RequestMethod.POST)
	public String  registerPost(@ModelAttribute("admin") @Valid User user, BindingResult bindingResult, Model model, Errors errors) {

		if (bindingResult.hasErrors()) {
			for (ObjectError objectError : bindingResult.getAllErrors()) {
				logger.info("error: ", objectError.getCode(), objectError.getDefaultMessage(),
						objectError.getObjectName());
			}
		} 
		else if(!user.getPassword().equals(user.getPasswordConfirm())) {
			model.addAttribute("passwordsAreNotSame",true);
		}
		else if (userRepository.findByLogin(user.getLogin()) != null) {
			model.addAttribute("loginAlreadyExists", true);
		} else if (userRepository.countByEmail(user.getEmail()) > 0) {
			model.addAttribute("emailAlreadyExists", true);
		} else if (!bindingResult.hasErrors()) {
			userRepository.save(user);
			return "redirect:/";
		}

		return "admin/register";
	}

	@RequestMapping(value = "settings")
	public String Settings(Model model) {

		model.addAttribute(systemEmail);


		return "system/settings";
	}

	@RequestMapping(value = "home")
	public String Home() {

		return "system/home";
	}
}
