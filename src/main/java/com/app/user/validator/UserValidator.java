package com.app.user.validator;

import com.app.announcement.repository.AnnouncementRepository;
import com.app.common.types.ValidatorCode;
import com.app.common.utils.validation.Result;
import com.app.common.utils.validation.ValidationDetails;
import com.app.common.validator.Validation;
import com.app.user.entity.User;
import com.app.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@AllArgsConstructor
public class UserValidator implements Validation<User> {
    private final UserRepository userRepository;
    private final AnnouncementRepository announcementRepository;

    @Override
    public Result<User> validateForDelete(User user) {
        var errors = createErrorsContainer();

        if (announcementRepository.existsByUserIdAndActiveIsTrue(user.getId())) {
            errors.put("announcements", ValidationDetails.of(ValidatorCode.HAVE_REF_OBJECTS));
        }

        return Result.create(errors).setValidatedObject(user);
    }

    @Override
    public Result<User> validateForSave(User user) {
        var errors = createErrorsContainer();

        errors.putAll(validatePassword(user.getPassword(), user.getPasswordConfirm()));
        errors.putAll(validateEmail(user.getEmail()));
        errors.putAll(checkLoginAlreadyExists(user));
        errors.putAll(checkEmailAlreadyExists(user));

        return Result.create(errors).setValidatedObject(user);
    }

    public Result<User> checkBeforeChangePass(User user) {
        var errors = createErrorsContainer();

        errors.putAll(validatePassword(user.getPassword(), user.getPasswordConfirm()));

        return Result.create(errors).setValidatedObject(user);
    }

    private Map<String, ValidationDetails> checkLoginAlreadyExists(User user) {
        var errors = createErrorsContainer();

        if (StringUtils.isNotBlank(user.getLogin()) &&
                ((user.getId() == null && userRepository.countByLogin(user.getLogin()) > 0) ||
                 (user.getId() != null && userRepository.countByLoginAndIdNot(user.getLogin(), user.getId()) > 0))) {
            errors.put("login", ValidationDetails.of(ValidatorCode.ALREADY_EXISTS, user.getLogin()));
        }

        return errors;
    }

    private Map<String, ValidationDetails> checkEmailAlreadyExists(User user) {
        var errors = createErrorsContainer();

        if (StringUtils.isNotBlank(user.getEmail()) &&
                (user.getId() == null && userRepository.countByEmail(user.getEmail()) > 0) ||
                (user.getId() != null && userRepository.countByEmailAndIdNot(user.getEmail(), user.getId()) > 0)) {
            errors.put("email", ValidationDetails.of(ValidatorCode.ALREADY_EXISTS, user.getEmail()));
        }

        return errors;
    }


    private Map<String, ValidationDetails> validateEmail(String email) {
        var errors = createErrorsContainer();

        if (StringUtils.isBlank(email)) {
            errors.put("email", ValidationDetails.of(ValidatorCode.IS_EMPTY, email));
        } else if (!EmailValidator.getInstance().isValid(email)) {
            errors.put("email", ValidationDetails.of(ValidatorCode.IS_NOT_VALID, email));
        }

        return errors;
    }

    private Map<String, ValidationDetails> validatePassword(String password, String passwordConfirm) {
        var errors = createErrorsContainer();

        if (password == null && passwordConfirm == null)
            return errors;

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
}
