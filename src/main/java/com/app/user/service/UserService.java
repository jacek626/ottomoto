package com.app.user.service;

import com.app.common.utils.validation.Result;
import com.app.security.entity.Role;
import com.app.security.repository.RoleRepository;
import com.app.user.entity.User;
import com.app.user.repository.UserRepository;
import com.app.user.validator.UserValidator;
import com.app.verification.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;
    private final VerificationTokenRepository verificationTokenRepository;

    public Result deleteUser(User user) {
        return userValidator
                .validateForDelete(user)
                .ifSuccess(() -> userRepository.delete(user));
    }

	public Result saveNewUser(User user) {
		setUserRoleIfRoleIsEmpty(user);

		return saveUser(user);
	}

	public Result<User> saveUser(User user) {
        return userValidator.validateForSave(user).ifSuccess(result -> {
            if (isUserExists(user)) {
                usePassFromDatabase(user);
            }
            else {
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            }

            userRepository.save(user);
        });
    }

    @Transactional
	public Result activate(String token) {
		Result result = Result.success();

		verificationTokenRepository.findByToken(token).ifPresentOrElse(
				verification -> {
					verification.getUser().setActive(true);
                    userRepository.save(verification.getUser());
                    verificationTokenRepository.delete(verification);
                },
                result::changeStatusToError);

        return result;
    }

    private boolean isUserExists(User user) {
        return user.getId() != null && user.getPassword() == null && user.getPasswordConfirm() == null;
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
        return userValidator
                .checkBeforeChangePass(user)
                .ifSuccess(() -> userRepository.updatePassword(bCryptPasswordEncoder.encode(user.getPassword()), user.getId()));
    }
}
