package com.app.validator;

import com.app.entity.Role;
import com.app.entity.User;
import com.app.enums.ValidatorCode;
import com.app.repository.AnnouncementRepository;
import com.app.repository.UserRepository;
import com.app.utils.validation.Result;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserValidatorTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AnnouncementRepository announcementRepository;

    @InjectMocks
    private UserValidator userValidator;

    @Test
    public void shouldReturnErrorBecPasswordsAreNotSame() {
        //given
        User user = User.builder().id(1L).password("test123").passwordConfirm("test12345").build();

        //when
        Result result = userValidator.checkBeforeChangePass(user);

        //then
        assertThat(result.isError()).isTrue();
        verify(userRepository, never()).updatePassword(any(String.class), any(Long.class));
    }


    @Test
    public void shouldReturnErrorDuringDeleteUserBecauseUserNotExistsOrIsDeactivated() {
        //given
        User user = prepareUser();
        user.setId(-1L);
        when(announcementRepository.existsByUserIdAndActiveIsTrue(any(Long.class))).thenReturn(true);

        //when
        Result result = userValidator.checkBeforeDelete(user);

        //then
        assertThat(result.isError()).isTrue();
        verify(userRepository, never()).delete(user);
    }


    @Test
    public void shouldReturnErrorBecausePasswordConfirmIsEmpty() {
        //given
        User user = prepareUser();
        user.setPasswordConfirm("");

        //when
        Result result = userValidator.checkBeforeSave(user);

        //then
        assertThat(result.isError()).isTrue();
        assertThat(result.getDetail("passwordConfirm").getValidatorCode()).isEqualTo(ValidatorCode.IS_EMPTY);
        verify(userRepository, never()).save(user);
    }

    @Test
    public void shouldReturnErrorBecauseEmailIsNotValidCase1() {
        //given
        User user = prepareUser();
        user.setEmail("test@");

        //when
        Result result = userValidator.checkBeforeSave(user);

        //then
        assertThat(result.isError()).isTrue();
        assertThat(result.getDetail("email").getValidatorCode()).isEqualTo(ValidatorCode.IS_NOT_VALID);
        verify(userRepository, never()).save(user);
    }

    @Test
    public void shouldReturnErrorBecauseEmailIsNotValidCase2() {
        //given
        User user = prepareUser();
        user.setEmail("test@test");

        //when
        Result result = userValidator.checkBeforeSave(user);

        //then
        assertThat(result.isError()).isTrue();
        assertThat(result.getDetail("email").getValidatorCode()).isEqualTo(ValidatorCode.IS_NOT_VALID);
        verify(userRepository, never()).save(user);
    }

    @Test
    public void shouldReturnErrorBecauseEmailIsNotValidCase3() {
        //given
        User user = prepareUser();
        user.setEmail("testtest.pl");

        //when
        Result result = userValidator.checkBeforeSave(user);

        //then
        assertThat(result.isError()).isTrue();
        assertThat(result.getDetail("email").getValidatorCode()).isEqualTo(ValidatorCode.IS_NOT_VALID);
        verify(userRepository, never()).save(user);
    }

    @Test
    public void shouldReturnErrorBecauseEmailIsNotValidCase4() {
        //given
        User user = prepareUser();
        user.setEmail("@test.pl");

        //when
        Result result = userValidator.checkBeforeSave(user);

        //then
        assertThat(result.isError()).isTrue();
        assertThat(result.getDetail("email").getValidatorCode()).isEqualTo(ValidatorCode.IS_NOT_VALID);
        verify(userRepository, never()).save(user);
    }


    @Test
    public void shouldReturnErrorDuringUserEditionBecUserWithSameLoginExists() {
        //given
        User user = prepareUser();
        user.setId(-1L);
        when(userRepository.countByLoginAndIdNot(any(String.class), any(Long.class))).thenReturn(1);

        //when
        Result result = userValidator.checkBeforeSave(user);

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
        Result result = userValidator.checkBeforeSave(user);

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
        Result result = userValidator.checkBeforeSave(user);

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
        Result result = userValidator.checkBeforeSave(user);

        //then
        assertThat(result.isError()).isTrue();
        assertThat(result.getDetail("login").getValidatorCode()).isEqualTo(ValidatorCode.ALREADY_EXISTS);
        verify(userRepository, never()).save(user);
    }

    @Test
    public void shouldReturnErrorBecausePasswordAndPasswordConfirmAreNotSame() {
        //given
        User user = prepareUser();
        user.setPasswordConfirm("OTHERPASS");

        //when
        Result result = userValidator.checkBeforeSave(user);

        //then
        assertThat(result.isError()).isTrue();
        assertThat(result.getDetail("password").getValidatorCode()).isEqualTo(ValidatorCode.IS_NOT_SAME);
        verify(userRepository, never()).save(user);
    }


    private User prepareUser() {
        return User.builder().login("User").password("password").passwordConfirm("password").email("usert@mail.com").active(true).role(new Role()).build();
    }

}
