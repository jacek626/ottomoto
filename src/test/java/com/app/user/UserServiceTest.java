package com.app.user;

import com.app.announcement.repository.AnnouncementRepository;
import com.app.common.utils.validation.Result;
import com.app.security.entity.Role;
import com.app.user.entity.User;
import com.app.user.repository.UserRepository;
import com.app.user.service.UserService;
import com.app.user.validator.UserValidator;
import com.app.verification.entity.VerificationToken;
import com.app.verification.repository.VerificationTokenRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserServiceTest {
	@Mock
	@SuppressWarnings("unused")
	private VerificationTokenRepository verificationTokenRepository;

    @Spy
    @SuppressWarnings("unused")
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AnnouncementRepository announcementRepository;

    @Mock
    private UserValidator userValidator;

    @InjectMocks
    private UserService userService;

    private static Validator entityValidator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        entityValidator = factory.getValidator();
    }

    @BeforeEach
    public void mockEmailAndLoginValidation() {
        given(userValidator.checkBeforeChangePass(any())).willReturn(Result.success());
        given(userRepository.countByLogin(any(String.class))).willReturn(0);
	}

	@Test
	public void shouldSaveUser() {
        //given
        var user = prepareUser();
        given(userValidator.validateForSave(any(User.class))).willReturn(Result.success());

        //when
        var result = userService.saveUser(user);
        Set<ConstraintViolation<User>> userEntityValidation = entityValidator.validate(user);

        //then
        assertThat(userEntityValidation.size()).isZero();
        assertThat(result.isSuccess()).isTrue();
        verify(userRepository, times(1)).save(user);
    }

	@Test
	public void shouldActivateUser() {
		//given
		final var token = "94d83912-bf69-4ef4-a99d-eddb05bf4327";
		var verificationToken = VerificationToken.builder().token(token).user(User.builder().build()).build();
		when(verificationTokenRepository.findByToken(any(String.class))).thenReturn(Optional.of(verificationToken));

		//when
		Result result = userService.activate(token);

		//then
		assertThat(result.isSuccess()).isTrue();
	}

	private User prepareUser() {
		return User.builder().login("User").password("password").passwordConfirm("password").email("usert@mail.com").active(true).role(new Role()).build();
	}


	@Test
	public void shouldDeleteUser() {
        //given
        var user = prepareUser();
        given(announcementRepository.existsByUserIdAndActiveIsTrue(any(Long.class))).willReturn(false);
        given(userValidator.validateForDelete(any(User.class))).willReturn(Result.success());

        //when
        Result result = userService.deleteUser(user);

        //then
        assertThat(result.isSuccess()).isTrue();
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    public void shouldChangeUserPass() {
        //given
        var user = User.builder().id(1L).password("test123").passwordConfirm("test123").build();
        given(userValidator.checkBeforeChangePass(any(User.class))).willReturn(Result.success());

        //when
        var result = userService.changePass(user);

        //then
        assertThat(result.isSuccess()).isTrue();
        verify(userRepository, times(1)).updatePassword(any(String.class), any(Long.class));
    }
}
