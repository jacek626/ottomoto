package com.app.validator;

import com.app.entity.User;
import com.app.enums.ValidatorCode;
import com.app.repository.AnnouncementRepository;
import com.app.repository.UserRepository;
import com.app.utils.Result;
import com.app.utils.ValidationDetails;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;

import java.util.Map;

public class UserValidator implements ValidatorCommonMethods<User> {

    private final UserRepository userRepository;

    private final AnnouncementRepository announcementRepository;

    public UserValidator(UserRepository userRepository, AnnouncementRepository announcementRepository) {
        this.userRepository = userRepository;
        this.announcementRepository = announcementRepository;
    }

    @Override
    public Result checkBeforeDelete(User user) {
        var errors = createErrorMap();

        if (announcementRepository.existsByUserIdAndActiveIsTrue(user.getId()))
            errors.put("announcements", ValidationDetails.of(ValidatorCode.HAVE_REF_OBJECTS));

        return Result.create(errors);
    }

    @Override
    public Result checkBeforeSave(User user) {
        var errors = createErrorMap();

        errors.putAll(validatePassword(user.getPassword(), user.getPasswordConfirm()));
        errors.putAll(validateEmail(user.getEmail()));
        errors.putAll(checkLoginAlreadyExists(user));
        errors.putAll(checkEmailAlreadyExists(user));

        return Result.create(errors);
    }

    private Map<String, ValidationDetails> checkLoginAlreadyExists(User user) {
        var errors = createErrorMap();

        if (StringUtils.isNotBlank(user.getLogin()) &&
                ((user.getId() == null && userRepository.countByLogin(user.getLogin()) > 0) ||
                        (user.getId() != null && userRepository.countByLoginAndIdNot(user.getLogin(), user.getId()) > 0))) {
            errors.put("login", ValidationDetails.of(ValidatorCode.ALREADY_EXISTS, user.getLogin()));
        }

        return errors;
    }

    private Map<String, ValidationDetails> checkEmailAlreadyExists(User user) {
        var errors = createErrorMap();

        if (StringUtils.isNotBlank(user.getEmail()) &&
                (user.getId() == null && userRepository.countByEmail(user.getEmail()) > 0) ||
                (user.getId() != null && userRepository.countByEmailAndIdNot(user.getEmail(), user.getId()) > 0)) {
            errors.put("email", ValidationDetails.of(ValidatorCode.ALREADY_EXISTS, user.getEmail()));
        }

        return errors;
    }


    private Map<String, ValidationDetails> validateEmail(String email) {
        var errors = createErrorMap();

        if (StringUtils.isBlank(email)) {
            errors.put("email", ValidationDetails.of(ValidatorCode.IS_EMPTY, email));
        } else if (!EmailValidator.getInstance().isValid(email)) {
            errors.put("email", ValidationDetails.of(ValidatorCode.IS_NOT_VALID, email));
        }

        return errors;
    }

    private Map<String, ValidationDetails> validatePassword(String password, String passwordConfirm) {
        var errors = createErrorMap();

        if (StringUtils.isBlank(password)) {
            errors.put("password", ValidationDetails.of(ValidatorCode.IS_EMPTY, password));
        } else if (StringUtils.isBlank(passwordConfirm)) {
            errors.put("passwordConfirm", ValidationDetails.of(ValidatorCode.IS_EMPTY, passwordConfirm));
        } else if (!password.equals(passwordConfirm)) {
            errors.put("password", ValidationDetails.of(ValidatorCode.IS_NOT_SAME, passwordConfirm));
        } else if (password.length() < 6) {
            errors.put("password", ValidationDetails.of(ValidatorCode.IS_TOO_SHORT, password));
        }

        return errors;
    }

    public Result checkBeforeChangePass(User user) {
        var errors = createErrorMap();

        errors.putAll(validatePassword(user.getPassword(), user.getPasswordConfirm()));

        return Result.create(errors);
    }
}
