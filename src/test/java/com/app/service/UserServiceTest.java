package com.app.service;

import com.app.entity.Role;
import com.app.entity.User;
import com.app.entity.VerificationToken;
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

	private User prepareUser() {
		return User.builder().login("User").password("password").passwordConfirm("password").email("usert@mail.com").active(true).role(new Role()).build();
	}


	@Test
	public void shouldDeleteUser() {
        //given
        User user = prepareUser();
        when(announcementRepository.existsByUserIdAndActiveIsTrue(any(Long.class))).thenReturn(false);

        //when
        Result result = userService.deleteUser(user);

        //then
        assertThat(result.isSuccess()).isTrue();
        verify(userRepository, times(1)).delete(user);
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



}
