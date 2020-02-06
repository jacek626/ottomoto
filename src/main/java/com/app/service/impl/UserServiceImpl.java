package com.app.service.impl;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.LocaleResolver;

import com.app.entity.Role;
import com.app.entity.User;
import com.app.repository.RoleRepository;
import com.app.repository.UserRepository;
import com.app.service.EmailService;
import com.app.service.UserService;
import com.app.utils.Result;
import com.app.validator.UserValidator;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository; 
	
	@Autowired
	private UserValidator userValidator; 
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private EmailService emailService;
	
	 @Autowired
	 private MessageSource messageSource;
	 
		@Autowired
		LocaleResolver localeResolver;
	
	public Result deleteUser(User user) {
		Map<String,String> validationResult = userValidator.checkBeforeDelete(user.getId());
				
		if(validationResult.isEmpty()) {
			userRepository.delete(user);
		}
		
		return Result.create(validationResult);
	}
	
	
	public Result saveUser(User user) {
		Map<String,String> validationResult = userValidator.checkBeforeSave(user);
		
		if(validationResult.isEmpty()) {
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			ifRoleIsNotSetSetDefaultUserRole(user);
			
			userRepository.save(user);
		}
		
		return Result.create(validationResult);
	}
	
	private String prepareActivationLink() {
		String link = "";
		
		
		return link;
	}
	
	public Result sentEmailWithAccountActivationLink(User user) { 
		try {
			emailService.sendEmailFromSystem(messageSource.getMessage("activationEmailSubject", null, LocaleContextHolder.getLocale()), messageSource.getMessage("activationEmailText", null, LocaleContextHolder.getLocale()), user.getEmail());
		} catch (Exception e) {
			e.printStackTrace();
			
			return Result.returnError();
		}
		
		return Result.retunSuccess();
	}


	private void ifRoleIsNotSetSetDefaultUserRole(User user) {
		if(Objects.isNull(user.getRole())) {
			Role userRole = roleRepository.findByName("ROLE_USER");
			Objects.requireNonNull(userRole, "No role defined");
			user.setRole(userRole);
		}
	}
	

}
