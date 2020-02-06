package com.app.validator;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.entity.User;
import com.app.repository.AnnouncementRepository;
import com.app.repository.UserRepository;

@Component
public class UserValidator {
	
	@Autowired
	private UserRepository userRepository; 
	
	@Autowired
	private AnnouncementRepository announcementRepository; 

	public Map<String, String> checkBeforeSave(User user) {
	 	Map<String,String> errors = new HashMap<>();
	 	
	 	if(StringUtils.isBlank(user.getPassword())) {
	 		errors.put("password", "isEmpty");
	 	}
	 	else if(StringUtils.isBlank(user.getPasswordConfirm())) {
			 errors.put("passwordConfirm", "isEmpty");
		}
		else if(!user.getPassword().equals(user.getPasswordConfirm())) {
			 errors.put("password", "passwordsAreNotSame");
		}
		else if(StringUtils.isBlank(user.getEmail())) {
			errors.put("email", "isEmpty");
		}
		else if(!EmailValidator.getInstance().isValid(user.getEmail())) {
			errors.put("email", "isNotValid");
		}
		else if (user.getId() == null && StringUtils.isNotBlank(user.getLogin()) && userRepository.findByLogin(user.getLogin()) != null) {
			errors.put("login","loginAlreadyExists");
		}
		else if (user.getId() == null && StringUtils.isNotBlank(user.getEmail()) && userRepository.countByEmail(user.getEmail()) > 0) {
			errors.put("email","emailAlreadyExists");
		}
		
		return errors;
	}
	
	public Map<String, String> checkBeforeDelete(Long userId) {
		Map<String,String> errors = new HashMap<>();
		
		if(announcementRepository.existsByUserIdAndDeactivationDateIsNull(userId)) 
			errors.put("announcements","userHaveActiveAnnouncements");
			
		return errors;
	}
}
