package com.app.service;

import com.app.entity.Role;
import com.app.entity.User;
import com.app.repository.AnnouncementRepository;
import com.app.repository.RoleRepository;
import com.app.repository.UserRepository;
import com.app.repository.VerificationTokenRepository;
import com.app.utils.Result;
import com.app.utils.ValidationDetails;
import com.app.validator.UserValidator;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;

@Service("userService")
public class UserService {

	private final UserRepository userRepository;

	private final UserValidator userValidator;

	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	private final RoleRepository roleRepository;

	private final EmailService emailService;

	private final MessageSource messageSource;

	private final VerificationTokenRepository verificationTokenRepository;

	public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, RoleRepository roleRepository, EmailService emailService, AnnouncementRepository announcementRepository, MessageSource messageSource, VerificationTokenRepository verificationTokenRepository) {
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.roleRepository = roleRepository;
		this.emailService = emailService;
		this.messageSource = messageSource;
		this.verificationTokenRepository = verificationTokenRepository;
		this.userValidator = new UserValidator(userRepository, announcementRepository);
	}


	public Result deleteUser(User user) {
		Map<String, ValidationDetails> validationResult = userValidator.checkBeforeDelete(user.getId());

		if (validationResult.isEmpty()) {
			userRepository.delete(user);
		}

		return Result.create(validationResult);
	}

	public Result saveNewUser(User user) {
		setUserRoleIfRoleIsEmpty(user);
		Result result = saveUser(user);

		return result;
	}

	public Result saveUser(User user) {
		Map<String, ValidationDetails> validationResult = userValidator.checkBeforeSave(user);

		if (validationResult.isEmpty()) {
            if (user.getId() != null && user.getPassword() == null && user.getPasswordConfirm() == null)
                ifEditUsePassFormDb(user);
            else
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

            userRepository.save(user);
        }

		return Result.create(validationResult);
	}

	@Transactional
	public Result activate(String token) {
		Result result = Result.success();

		verificationTokenRepository.findByToken(token).ifPresentOrElse(
				e -> {
					e.getUser().setActive(true);
                    userRepository.save(e.getUser());
                    verificationTokenRepository.delete(e);
                },
                () -> {
                    result.changeStatusToError();
                });

        return result;
    }

    private void ifEditUsePassFormDb(User user) {
        if (user.getId() != null && user.getPassword() == null) {
            String password = userRepository.findPasswordById(user.getId());
            user.setPassword(password);
            user.setPasswordConfirm(password);
        }
    }

    private void setUserRoleIfRoleIsEmpty(User user) {
        if (Objects.isNull(user.getRole())) {
            Role userRole = roleRepository.findByName("ROLE_USER");
            Objects.requireNonNull(userRole, "No role defined");
            user.setRole(userRole);
        }
    }


    public Result changePass(User user) {
        Map<String, ValidationDetails> validationResult = userValidator.checkBeforeChangePass(user);

        if (validationResult.isEmpty()) {
            userRepository.updatePassword(bCryptPasswordEncoder.encode(user.getPassword()), user.getId());
        }

        return Result.create(validationResult);
    }
}
