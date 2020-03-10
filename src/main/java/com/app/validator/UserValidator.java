package com.app.validator;

import com.app.entity.User;
import com.app.enums.ValidatorCode;
import com.app.repository.AnnouncementRepository;
import com.app.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserValidator {
	
	@Autowired
	private UserRepository userRepository; 
	
	@Autowired
	private AnnouncementRepository announcementRepository; 

	public Map<String, ValidatorCode> checkBeforeSave(User user) {
	 	Map<String,ValidatorCode> errors = new HashMap<>();
	 	
	 	if(StringUtils.isBlank(user.getPassword())) {
	 		errors.put("password", ValidatorCode.IS_EMPTY);
	 	}
	 	else if(StringUtils.isBlank(user.getPasswordConfirm())) {
			 errors.put("passwordConfirm", ValidatorCode.IS_EMPTY);
		}
		else if(!user.getPassword().equals(user.getPasswordConfirm())) {
			 errors.put("password", ValidatorCode.IS_NOT_SAME);
		}
		else if(StringUtils.isBlank(user.getEmail())) {
			errors.put("email", ValidatorCode.IS_EMPTY);
		}
		else if(!EmailValidator.getInstance().isValid(user.getEmail())) {
			errors.put("email", ValidatorCode.IS_NOT_VALID);
		}
		else if (StringUtils.isNotBlank(user.getLogin()) &&
				((user.getId() == null && userRepository.countByLogin(user.getLogin()) > 0) ||
				(user.getId() != null && userRepository.countByLoginAndIdNot(user.getLogin(), user.getId()) > 0)))
		{
			errors.put("login", ValidatorCode.ALREADY_EXISTS);
		}
		else if (StringUtils.isNotBlank(user.getEmail()) &&
				(user.getId() == null && userRepository.countByEmail(user.getEmail()) > 0) ||
				(user.getId() != null && userRepository.countByEmailAndIdNot(user.getEmail(), user.getId()) > 0))
		{
			errors.put("email", ValidatorCode.ALREADY_EXISTS);
		}

		return errors;
	}
	
	public Map<String, ValidatorCode> checkBeforeDelete(Long userId) {
		Map<String,ValidatorCode> errors = new HashMap<>();
		
		if(announcementRepository.existsByUserIdAndDeactivationDateIsNull(userId)) 
			errors.put("announcements", ValidatorCode.HAVE_REF_OBJECTS);
			
		return errors;
	}
}
