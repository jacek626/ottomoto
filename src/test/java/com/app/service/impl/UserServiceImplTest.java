package com.app.service.impl;

import com.app.entity.Role;
import com.app.entity.User;
import com.app.enums.ValidatorCode;
import com.app.repository.AnnouncementRepository;
import com.app.repository.RoleRepository;
import com.app.repository.UserRepository;
import com.app.utils.Result;
import com.app.validator.UserValidator;
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
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserServiceImplTest {

	@Mock
	private RoleRepository roleRepository;
	
	@Spy
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Mock
	private UserRepository userRepository;

	@Mock
	private AnnouncementRepository announcementRepository;

	@InjectMocks
	private UserValidator userValidator = spy(UserValidator.class);

	@InjectMocks
	private UserServiceImpl userService;

	private static Validator validator;

	@BeforeAll
	static void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
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
		Set<ConstraintViolation<User>> userEntityValidation = validator.validate( user );

		//then
		assertThat(userEntityValidation.size()).isZero();
		assertThat(result.isSuccess()).isTrue();
		verify(userRepository, times(1)).save(user);
	}

	private User prepareUser() {
		return new User.UserBuilder("User", "password", "password", "usert@mail.com", true).setRole(new Role()).build();
	}


	@Test
	public void shouldReturnErrorBecUserWithSameLoginExists_idIsNull() {
		//given
		User user = prepareUser();
		when(userRepository.countByLogin(any(String.class))).thenReturn(1);

		//when
		Result result = userService.saveUser(user);

		//then
		assertThat(result.isError()).isTrue();
		assertThat(result.getDetail("login").getCode()).isEqualTo(ValidatorCode.ALREADY_EXISTS);
		verify(userRepository, never()).save(user);
	}

	@Test
	public void shouldReturnErrorBecUserWithSameLoginExists_idIsSet() {
		//given
		User user = prepareUser();
		user.setId(-1L);
		when(userRepository.countByLoginAndIdNot(any(String.class),any(Long.class))).thenReturn(1);

		//when
		Result result = userService.saveUser(user);

		//then
		assertThat(result.isError()).isTrue();
		assertThat(result.getDetail("login").getCode()).isEqualTo(ValidatorCode.ALREADY_EXISTS);
		verify(userRepository, never()).save(user);
	}

	@Test
	public void shouldReturnErrorBecUserWithSameEmailExists_idIsNull() {
		//given
		User user = prepareUser();
		when(userRepository.countByEmail(any(String.class))).thenReturn(1);

		//when
		Result result = userService.saveUser(user);

		//then
		assertThat(result.isError()).isTrue();
		assertThat(result.getDetail("email").getCode()).isEqualTo(ValidatorCode.ALREADY_EXISTS);
		verify(userRepository, never()).save(user);
	}

	@Test
	public void shouldReturnErrorBecUserWithSameEmailExists_idIsSet() {
		//given
		User user = prepareUser();
		user.setId(-1L);
		when(userRepository.countByEmailAndIdNot(any(String.class), any(Long.class))).thenReturn(1);

		//when
		Result result = userService.saveUser(user);

		//then
		assertThat(result.isError()).isTrue();
		assertThat(result.getDetail("email").getCode()).isEqualTo(ValidatorCode.ALREADY_EXISTS);
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
		assertThat(result.getDetail("password").getCode()).isEqualTo(ValidatorCode.IS_NOT_SAME);
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
		assertThat(result.getDetail("passwordConfirm").getCode()).isEqualTo(ValidatorCode.IS_EMPTY);
		verify(userRepository, never()).save(user);
	}

	@Test
	public void shouldReturnValidationErrorBecauseEmailIsNotValid_test_1() {
		//given
		User user = prepareUser();
		user.setEmail("test@");

		//when
		Result result = userService.saveUser(user);

		//then
		assertThat(result.isError()).isTrue();
		assertThat(result.getDetail("email").getCode()).isEqualTo(ValidatorCode.IS_NOT_VALID);
		verify(userRepository, never()).save(user);
	}

	@Test
	public void shouldReturnValidationErrorBecauseEmailIsNotValid_test_2() {
		//given
		User user = prepareUser();
		user.setEmail("test@test");

		//when
		Result result = userService.saveUser(user);

		//then
		assertThat(result.isError()).isTrue();
		assertThat(result.getDetail("email").getCode()).isEqualTo(ValidatorCode.IS_NOT_VALID);
		verify(userRepository, never()).save(user);
	}

	@Test
	public void shouldReturnValidationErrorBecauseEmailIsNotValid_test_3() {
		//given
		User user = prepareUser();
		user.setEmail("testtest.pl");

		//when
		Result result = userService.saveUser(user);

		//then
		assertThat(result.isError()).isTrue();
		assertThat(result.getDetail("email").getCode()).isEqualTo(ValidatorCode.IS_NOT_VALID);
		verify(userRepository, never()).save(user);
	}

	@Test
	public void shouldReturnValidationErrorBecauseEmailIsNotValid_test_4() {
		//given
		User user = prepareUser();
		user.setEmail("@test.pl");

		//when
		Result result = userService.saveUser(user);

		//then
		assertThat(result.isError()).isTrue();
		assertThat(result.getDetail("email").getCode()).isEqualTo(ValidatorCode.IS_NOT_VALID);
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


	// dorobic edycje, i popatrzec czy educja w innych jest



}
