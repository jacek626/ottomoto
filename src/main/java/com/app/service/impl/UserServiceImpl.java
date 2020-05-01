package com.app.service.impl;

import com.app.entity.Role;
import com.app.entity.User;
import com.app.repository.RoleRepository;
import com.app.repository.UserRepository;
import com.app.service.EmailService;
import com.app.service.UserService;
import com.app.utils.Result;
import com.app.utils.ValidationDetails;
import com.app.validator.UserValidator;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service("userService")
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	
	private final UserValidator userValidator;
	
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	private final RoleRepository roleRepository;
	
	private final EmailService emailService;

	private final MessageSource messageSource;

	public UserServiceImpl(UserRepository userRepository, UserValidator userValidator, BCryptPasswordEncoder bCryptPasswordEncoder, RoleRepository roleRepository, EmailService emailService, MessageSource messageSource) {
		this.userRepository = userRepository;
		this.userValidator = userValidator;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.roleRepository = roleRepository;
		this.emailService = emailService;
		this.messageSource = messageSource;
	}


	public Result deleteUser(User user) {
		Map<String, ValidationDetails> validationResult = userValidator.checkBeforeDelete(user.getId());
				
		if(validationResult.isEmpty()) {
			userRepository.delete(user);
		}
		
		return Result.create(validationResult);
	}
	
	
	public Result saveUser(User user) {
		Map<String, ValidationDetails> validationResult = userValidator.checkBeforeSave(user);
		
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
/*		try {
			emailService.sendEmailFromSystemEmail(messageSource.getMessage("activationEmailSubject", null, LocaleContextHolder.getLocale()), messageSource.getMessage("activationEmailText", null, LocaleContextHolder.getLocale()), user.getEmail());
		} catch (Exception e) {
			e.printStackTrace();

			return Result.error();
		}

		return Result.success();*/
		return Result.success();
	}


	private void ifRoleIsNotSetSetDefaultUserRole(User user) {
		if(Objects.isNull(user.getRole())) {
			Role userRole = roleRepository.findByName("ROLE_USER");
			Objects.requireNonNull(userRole, "No role defined");
			user.setRole(userRole);
		}
	}
	

}
