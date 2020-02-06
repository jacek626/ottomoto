package com.app.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.app.entity.Role;
import com.app.entity.User;
import com.app.repository.RoleRepository;
import com.app.repository.UserRepository;
import com.app.utils.Result;
import com.app.validator.UserValidator;

//@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserServiceImplTest {

	@Mock
	private UserRepository userRepositoryMock;
	
	@Mock
	private UserValidator userValidatorMock;
	
	@Mock
	private RoleRepository roleRepositoryMock;
	
	@Mock
	private BCryptPasswordEncoder bCryptPasswordEncoderMock;
	
	@InjectMocks
	private UserServiceImpl userServiceMock;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserServiceImpl userService;
	
	private static Validator validator;
	
	private static User SAVED_USER;
	
	@BeforeAll
	static void setUp() throws Exception {
		
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        SAVED_USER = new User();
        SAVED_USER.setId(-1L);
	}

	@Test
	public void shouldSaveSingleAUser() {
		User user = new User.UserBuilder("userLoginTest","testPass","testPass","mailTest@test.com", true).setRole(new Role()).build();
		
//		when(userRepository.save(any(User.class))).thenAnswer(e -> { User user2 = new User(); user.setId(1L); return user; });
		/*
		 * when(userRepository.save(any(User.class))).thenAnswer(e -> {
		 * 
		 * User user2 = new User(); user.setId(1L); return user;
		 * 
		 * });
		 */
		
		Result result = userServiceMock.saveUser(user);
		// ifRoleIsNotSetSetDefaultUserRole
		
		verify(userRepositoryMock, times(1)).save(any(User.class));
		
		assertTrue(result.isSuccess()); 
	//	assertNotNull(user.getId());

	//	Set<ConstraintViolation<User>> violations = validator.validate(user);
	//	 assertTrue(violations.isEmpty()); 
		
	//	assertTrue(validationResult.isEmpty());
		//assertNotNull(user.getId());
	}
	/*
	 * public void shouldSaveSingleAUser() { User user = new
	 * User.UserBuilder("userLoginTest","testPass","testPass","mailTest@test.com",
	 * true).build();
	 * 
	 * Map<String,String> validationResult = userService.saveUser(user);
	 * 
	 * assertTrue(validationResult.isEmpty()); assertNotNull(user.getId()); }
	 */	
	
	// Mockito.verify(foo, Mockito.times(1)).add("1");
	
	@Test
	public void shouldSaveTwoUsers() {
		User user1 = new User.UserBuilder("uSTuserLoginTest1","testPass","testPass","uSTmailTest1@test.com", true).setRole(new Role()).build();
		User user2 = new User.UserBuilder("uSTuserLoginTest2","testPass","testPass","uSTmailTest2@test.com", true).setRole(new Role()).build();
		
		when(userRepositoryMock.save(any(User.class))).thenReturn(SAVED_USER);
	//	when(userRepository.save(any(User.class))).thenAnswer(e -> { User user = new User(); user.setId(1L); return user; });
		
		Result resultForUser1 = userServiceMock.saveUser(user1);
		Result resultForUser2 = userServiceMock.saveUser(user2);
		
		verify(userRepositoryMock, times(2)).save(any(User.class));
		
		assertTrue(resultForUser1.isSuccess());
		assertTrue(resultForUser2.isSuccess());
	}
	
	@Test
	public void shouldReturnValidationErrorDuringSaveSecondUserBecouseLoginsAreSame() {
		User user1 = new User.UserBuilder("uSTuserLoginTest3","testPass","testPass","uSTmailTest3@test.com", true).build();
		User user2 = new User.UserBuilder("uSTuserLoginTest3","testPass","testPass","uSTmailTest3.1@test.com", true).build();
		
		Result result1 = userService.saveUser(user1);
		Result result2 = userService.saveUser(user2);
		
		assertTrue(result1.isSuccess());
		assertNotNull(user1.getId());
		//verify(userRepositoryMock, times(1)).save(any(User.class));
		
		assertTrue(result2.isError());
		assertTrue(result2.getValidationResult().get("login").equals("loginAlreadyExists"));
		assertNull(user2.getId());
	}
	
	@Test
	public void shouldReturnValidationErrorDuringSaveSecondUserBecouseEmailsAreSame() {
		User user1 = new User.UserBuilder("uSTuserLoginTest4","testPass","testPass","uSTmailTest4@test.com", true).build();
		User user2 = new User.UserBuilder("uSTuserLoginTest4.1","testPass","testPass","uSTmailTest4@test.com", true).build();
		
		Result result1 = userService.saveUser(user1);
		Result result2 = userService.saveUser(user2);
		
		assertTrue(result1.isSuccess());
		assertNotNull(user1.getId());
		
		assertTrue(result2.isError());
		assertTrue(result2.getValidationResult().get("email").equals("emailAlreadyExists"));
		assertNull(user2.getId());
	}
	
	@Test
	public void shouldReturnValidationErrorBecousePasswordsAreNotSame() {
		User user1 = new User.UserBuilder("uSTuserLoginTest5","testPass","testPass2","uSTmailTest5@test.com", true).build();
		
		Result result = userService.saveUser(user1);
		
		assertTrue(result.isError());
		assertTrue(result.getValidationResult().get("password").equals("passwordsAreNotSame"));
		assertNull(user1.getId());
		
	}
	
	@Test
	public void shouldReturnValidationErrorBecousePasswordConfirmIsEmpty() {
		User user1 = new User.UserBuilder("uSTuserLoginTest6","testPass","","uSTmailTest6@test.com", true).build();
		
		Result result = userService.saveUser(user1);
		
		assertTrue(result.isError());
		assertTrue(result.getValidationResult().get("passwordConfirm").equals("isEmpty"));
		assertNull(user1.getId());
	}
	
	@Test
	public void shouldReturnValidationErrorBecousePasswordConfirmIsNull() {
		User user1 = new User.UserBuilder("uSTuserLoginTest7","testPass",null,"uSTmailTest7@test.com", true).build();
		
		Result result = userService.saveUser(user1);
		
		assertTrue(result.isError());
		assertTrue(result.getValidationResult().get("passwordConfirm").equals("isEmpty"));
		assertNull(user1.getId());
	}
	
	@Test
	public void shouldReturnValidationErrorBecousePasswordIsEmpty() {
		User user1 = new User.UserBuilder("uSTuserLoginTest8",null,"testPass","uSTmailTest8@test.com", true).build();
		
		Result result = userService.saveUser(user1);
		
		assertTrue(result.isError());
		assertTrue(result.getValidationResult().get("password").equals("isEmpty"));
		assertNull(user1.getId());
	}
	
	@Test
	public void shouldReturnValidationErrorBecousePasswordIsNull() {
		User user1 = new User.UserBuilder("uSTuserLoginTest9", null ,"testPass","uSTmailTest9@test.com", true).build();
		
		Result result = userService.saveUser(user1);
		
		assertTrue(result.isError());
		assertTrue(result.getValidationResult().get("password").equals("isEmpty"));
		assertNull(user1.getId());
	}
	
	@Test
	public void shouldReturnValidationErrorBecouseEmailIsNull() {
		User user1 = new User.UserBuilder("uSTuserLoginTest10", "testPass" ,"testPass", null, true).build();
		
		Result result = userService.saveUser(user1);
		
		assertTrue(result.isError());
		assertTrue(result.getValidationResult().get("email").equals("isEmpty"));
		assertNull(user1.getId());
	}
	
	@Test
	public void shouldReturnValidationErrorBecouseEmailIsNotValid() {
		User user1 = new User.UserBuilder("uSTuserLoginTest11", "testPass" ,"testPass","uSTmailTest11test.com", true).build();
		
		Result result = userService.saveUser(user1);
		
		assertTrue(result.isError());
		assertTrue(result.getValidationResult().get("email").equals("isNotValid"));
		assertNull(user1.getId());
	}
	
	@Test
	public void shouldDeleteUser() {
		User user1 = new User.UserBuilder("uSTuserLoginTest12", "testPass" ,"testPass","uSTmailTest12@test.com", true).build();
		
		Result result = userService.saveUser(user1);
		userService.deleteUser(user1);
		
		assertTrue(result.isSuccess());
		assertNotNull(user1.getId());
		assertFalse(userRepository.findById(user1.getId()).isPresent());
	}
	
	@Test
	public void shouldReturnExceptionThatUserNotExists() {
		User user1 = new User.UserBuilder("uSTuserLoginTest13", "testPass" ,"testPass","uSTmailTest13@test.com", true).build();
		
		userService.deleteUser(user1);
		
	}
	
}
