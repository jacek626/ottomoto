package com.app.validator;

import com.app.entity.User;
import com.app.enums.ValidatorCode;
import com.app.repository.AnnouncementRepository;
import com.app.repository.UserRepository;
import com.app.utils.ValidationDetails;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;

import java.util.HashMap;
import java.util.Map;

public class UserValidator {

    private final UserRepository userRepository;

    private final AnnouncementRepository announcementRepository;

    public UserValidator(UserRepository userRepository, AnnouncementRepository announcementRepository) {
        this.userRepository = userRepository;
        this.announcementRepository = announcementRepository;
    }

    public Map<String, ValidationDetails> checkBeforeSave(User user) {
        Map<String, ValidationDetails> errors = new HashMap<>();

        if (StringUtils.isBlank(user.getPassword())) {
            errors.put("password", ValidationDetails.of(ValidatorCode.IS_EMPTY, user.getPassword()));
        } else if (StringUtils.isBlank(user.getPasswordConfirm())) {
            errors.put("passwordConfirm", ValidationDetails.of(ValidatorCode.IS_EMPTY, user.getPasswordConfirm()));
        }
		else if(!user.getPassword().equals(user.getPasswordConfirm())) {
            errors.put("password", ValidationDetails.of(ValidatorCode.IS_NOT_SAME, user.getPasswordConfirm()));
        }
		else if(StringUtils.isBlank(user.getEmail())) {
            errors.put("email", ValidationDetails.of(ValidatorCode.IS_EMPTY, user.getEmail()));
        }
		else if(!EmailValidator.getInstance().isValid(user.getEmail())) {
            errors.put("email", ValidationDetails.of(ValidatorCode.IS_NOT_VALID, user.getEmail()));
        }
		else if (StringUtils.isNotBlank(user.getLogin()) &&
				((user.getId() == null && userRepository.countByLogin(user.getLogin()) > 0) ||
				(user.getId() != null && userRepository.countByLoginAndIdNot(user.getLogin(), user.getId()) > 0))) {
            errors.put("login", ValidationDetails.of(ValidatorCode.ALREADY_EXISTS, user.getLogin()));
        }
		else if (StringUtils.isNotBlank(user.getEmail()) &&
				(user.getId() == null && userRepository.countByEmail(user.getEmail()) > 0) ||
				(user.getId() != null && userRepository.countByEmailAndIdNot(user.getEmail(), user.getId()) > 0)) {
            errors.put("email", ValidationDetails.of(ValidatorCode.ALREADY_EXISTS, user.getEmail()));
        }

		return errors;
	}
	
	public Map<String, ValidationDetails> checkBeforeDelete(Long userId) {
		Map<String, ValidationDetails> errors = new HashMap<>();
		
		if(announcementRepository.existsByUserIdAndDeactivationDateIsNull(userId)) 
			errors.put("announcements", ValidationDetails.of(ValidatorCode.HAVE_REF_OBJECTS));
			
		return errors;
	}
}
