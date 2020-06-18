package com.app.service;

import com.app.entity.Role;
import com.app.entity.User;
import com.app.entity.VerificationToken;
import com.app.enums.ValidatorCode;
import com.app.repository.AnnouncementRepository;
import com.app.repository.RoleRepository;
import com.app.repository.UserRepository;
import com.app.repository.VerificationTokenRepository;
import com.app.utils.Result;
import com.app.utils.SystemEmail;
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
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserServiceTest {

	@Mock
	@SuppressWarnings("unused")
	private RoleRepository roleRepository;

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

    private static Validator entityValidator;

    @InjectMocks
    private UserService userService;
    @Mock
    private SystemEmail systemEmail;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        entityValidator = factory.getValidator();
    }

    @BeforeEach
    public void mockEmailAndLoginValidation() {
        when(userRepository.countByLogin(any(String.class))).thenReturn(0);
	}

	@Test
	public void shouldSaveUser() {
        //given
        User user = prepareUser();

        //when
        Result result = userService.saveUser(user);
        Set<ConstraintViolation<User>> userEntityValidation = entityValidator.validate(user);

        //then
        assertThat(userEntityValidation.size()).isZero();
        assertThat(result.isSuccess()).isTrue();
        verify(userRepository, times(1)).save(user);
    }


	@Test
	public void shouldActivateUser() {
		//given
		String token = "94d83912-bf69-4ef4-a99d-eddb05bf4327";
		var verificationToken = VerificationToken.builder().token(token).user(User.builder().build()).build();
		when(verificationTokenRepository.findByToken(any(String.class))).thenReturn(Optional.of(verificationToken));

		//when
		Result result = userService.activate(token);

		//then
		assertThat(result.isSuccess()).isTrue();
	}

	@Test
	public void shouldReturnErrorDuringUserActivationBecTokenNotExists() {
		//given
		String token = "94d83912-bf69-4ef4-a99d-eddb05bf4327";
		when(verificationTokenRepository.findByToken(any(String.class))).thenReturn(Optional.ofNullable(null));

		//when
		Result result = userService.activate(token);

		//then
		assertThat(result.isError()).isTrue();
	}

	private User prepareUser() {
		return User.builder().login("User").password("password").passwordConfirm("password").email("usert@mail.com").active(true).role(new Role()).build();
	}


	@Test
	public void shouldReturnErrorDuringUserEditionBecUserWithSameLoginExists() {
		//given
		User user = prepareUser();
		user.setId(-1L);
		when(userRepository.countByLoginAndIdNot(any(String.class), any(Long.class))).thenReturn(1);

		//when
		Result result = userService.saveUser(user);

		//then
		assertThat(result.isError()).isTrue();
		assertThat(result.getDetail("login").getValidatorCode()).isEqualTo(ValidatorCode.ALREADY_EXISTS);
		verify(userRepository, never()).save(user);
	}

	@Test
	public void shouldReturnErrorDuringUserEditionBecUserWithSameEmailExists() {
		//given
		User user = prepareUser();
		user.setId(-1L);
		when(userRepository.countByEmailAndIdNot(any(String.class), any(Long.class))).thenReturn(1);

		//when
		Result result = userService.saveUser(user);

		//then
		assertThat(result.isError()).isTrue();
		assertThat(result.getDetail("email").getValidatorCode()).isEqualTo(ValidatorCode.ALREADY_EXISTS);
		verify(userRepository, never()).save(user);
	}

	@Test
	public void shouldReturnErrorDuringUserCreationBecUserWithSameEmailExists() {
		//given
		User user = prepareUser();
		when(userRepository.countByEmail(any(String.class))).thenReturn(1);

		//when
		Result result = userService.saveUser(user);

		//then
		assertThat(result.isError()).isTrue();
		assertThat(result.getDetail("email").getValidatorCode()).isEqualTo(ValidatorCode.ALREADY_EXISTS);
		verify(userRepository, never()).save(user);
	}


	@Test
	public void shouldReturnErrorDuringUserCreationBecUserWithSameLoginExists() {
		//given
		User user = prepareUser();
		when(userRepository.countByLogin(any(String.class))).thenReturn(1);

		//when
		Result result = userService.saveUser(user);

		//then
		assertThat(result.isError()).isTrue();
		assertThat(result.getDetail("login").getValidatorCode()).isEqualTo(ValidatorCode.ALREADY_EXISTS);
		verify(userRepository, never()).save(user);
	}

	@Test
	public void shouldReturnValidationErrorBecausePasswordAndPasswordConfirmAreNotSame() {
		//given
		User user = prepareUser();
		user.setPasswordConfirm("OTHERPASS");

		//when
		Result result = userService.saveUser(user);

		//then
		assertThat(result.isError()).isTrue();
		assertThat(result.getDetail("password").getValidatorCode()).isEqualTo(ValidatorCode.IS_NOT_SAME);
		verify(userRepository, never()).save(user);
	}

	@Test
	public void shouldReturnValidationErrorBecausePasswordConfirmIsEmpty() {
		//given
		User user = prepareUser();
		user.setPasswordConfirm("");

		//when
		Result result = userService.saveUser(user);

		//then
		assertThat(result.isError()).isTrue();
		assertThat(result.getDetail("passwordConfirm").getValidatorCode()).isEqualTo(ValidatorCode.IS_EMPTY);
		verify(userRepository, never()).save(user);
	}

	@Test
	public void shouldReturnValidationErrorBecauseEmailIsNotValidCase1() {
		//given
		User user = prepareUser();
		user.setEmail("test@");

		//when
		Result result = userService.saveUser(user);

		//then
		assertThat(result.isError()).isTrue();
		assertThat(result.getDetail("email").getValidatorCode()).isEqualTo(ValidatorCode.IS_NOT_VALID);
		verify(userRepository, never()).save(user);
	}

	@Test
	public void shouldReturnValidationErrorBecauseEmailIsNotValidCase2() {
		//given
		User user = prepareUser();
		user.setEmail("test@test");

		//when
		Result result = userService.saveUser(user);

		//then
		assertThat(result.isError()).isTrue();
		assertThat(result.getDetail("email").getValidatorCode()).isEqualTo(ValidatorCode.IS_NOT_VALID);
		verify(userRepository, never()).save(user);
	}

	@Test
	public void shouldReturnValidationErrorBecauseEmailIsNotValidCase3() {
		//given
		User user = prepareUser();
		user.setEmail("testtest.pl");

		//when
		Result result = userService.saveUser(user);

		//then
		assertThat(result.isError()).isTrue();
		assertThat(result.getDetail("email").getValidatorCode()).isEqualTo(ValidatorCode.IS_NOT_VALID);
		verify(userRepository, never()).save(user);
	}

	@Test
	public void shouldReturnValidationErrorBecauseEmailIsNotValidCase4() {
		//given
		User user = prepareUser();
		user.setEmail("@test.pl");

		//when
		Result result = userService.saveUser(user);

		//then
		assertThat(result.isError()).isTrue();
		assertThat(result.getDetail("email").getValidatorCode()).isEqualTo(ValidatorCode.IS_NOT_VALID);
		verify(userRepository, never()).save(user);
	}


	@Test
	public void shouldDeleteUser() {
		//given
		User user = prepareUser();
		when(announcementRepository.existsByUserIdAndDeactivationDateIsNull(any(Long.class))).thenReturn(false);

		//when
		Result result = userService.deleteUser(user);

		//then
		assertThat(result.isSuccess()).isTrue();
		verify(userRepository, times(1)).delete(user);
	}

	@Test
	public void shouldReturnErrorDuringDeleteUserBecauseUserNotExistsOrIsDeactivated() {
		//given
		User user = prepareUser();
		user.setId(-1L);
        when(announcementRepository.existsByUserIdAndDeactivationDateIsNull(any(Long.class))).thenReturn(true);

        //when
        Result result = userService.deleteUser(user);

        //then
        assertThat(result.isError()).isTrue();
        verify(userRepository, never()).delete(user);
    }

    @Test
    public void shouldChangeUserPass() {
        //given
        User user = User.builder().id(1L).password("test123").passwordConfirm("test123").build();

        //when
        Result result = userService.changePass(user);

        //then
        assertThat(result.isSuccess()).isTrue();
        verify(userRepository, times(1)).updatePassword(any(String.class), any(Long.class));
    }

    @Test
    public void shouldReturnErrorBecPasswordsAreNotSame() {
        //given
        User user = User.builder().id(1L).password("test123").passwordConfirm("test12345").build();

        //when
        Result result = userService.changePass(user);

        //then
        assertThat(result.isError()).isTrue();
        verify(userRepository, never()).updatePassword(any(String.class), any(Long.class));
    }

}
