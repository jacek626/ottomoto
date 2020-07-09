package com.app.service;

import com.app.entity.Role;
import com.app.entity.User;
import com.app.repository.AnnouncementRepository;
import com.app.repository.RoleRepository;
import com.app.repository.UserRepository;
import com.app.repository.VerificationTokenRepository;
import com.app.utils.validation.Result;
import com.app.validator.UserValidator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service("userService")
public class UserService {

    private final UserRepository userRepository;

    private final UserValidator userValidator;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final RoleRepository roleRepository;

    private final VerificationTokenRepository verificationTokenRepository;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, RoleRepository roleRepository, AnnouncementRepository announcementRepository, VerificationTokenRepository verificationTokenRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.userValidator = new UserValidator(userRepository, announcementRepository);
    }


    public Result deleteUser(User user) {
        Result<User> result = userValidator.checkBeforeDelete(user);

        if (result.isSuccess()) {
            userRepository.delete(user);
        }

        return result;
    }

	public Result saveNewUser(User user) {
		setUserRoleIfRoleIsEmpty(user);

		Result result = saveUser(user);

		return result;
	}

	public Result saveUser(User user) {
        Result<User> result = userValidator.checkBeforeSave(user);

        if (result.isSuccess()) {
            if (user.getId() != null && user.getPassword() == null && user.getPasswordConfirm() == null)
                usePassFromDatabase(user);
            else
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

            userRepository.save(user);
        }

        return result;
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

    private void usePassFromDatabase(User user) {
        String password = userRepository.findPasswordById(user.getId());
        user.setPassword(password);
        user.setPasswordConfirm(password);
    }

    private void setUserRoleIfRoleIsEmpty(User user) {
        if (Objects.isNull(user.getRole())) {
            Role userRole = roleRepository.findByName("ROLE_USER");
            Objects.requireNonNull(userRole, "No role defined");
            user.setRole(userRole);
        }
    }


    public Result changePass(User user) {
        Result result = userValidator.checkBeforeChangePass(user);

        if (result.isSuccess()) {
            userRepository.updatePassword(bCryptPasswordEncoder.encode(user.getPassword()), user.getId());
        }

        return result;
    }
}
