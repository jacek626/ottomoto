package com.app.user;

import com.app.announcement.repository.AnnouncementRepository;
import com.app.common.types.ValidatorCode;
import com.app.security.entity.Role;
import com.app.user.entity.User;
import com.app.user.repository.UserRepository;
import com.app.user.validator.UserValidator;
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
    public void shouldReturnValidationErrorWhenPasswordsAreNotSame() {
        //given
        User user = User.builder().id(1L).password("test123").passwordConfirm("test12345").build();

        //when
        var result = userValidator.checkBeforeChangePass(user);

        //then
        assertThat(result.isError()).isTrue();
        verify(userRepository, never()).updatePassword(any(String.class), any(Long.class));
    }


    @Test
    public void shouldReturnValidationErrorDuringDeleteUserBecauseUserNotExistsOrIsDeactivated() {
        //given
        User user = prepareUser();
        user.setId(-1L);
        when(announcementRepository.existsByUserIdAndActiveIsTrue(any(Long.class))).thenReturn(true);

        //when
        var result = userValidator.validateForDelete(user);

        //then
        assertThat(result.isError()).isTrue();
        verify(userRepository, never()).delete(user);
    }


    @Test
    public void shouldReturnValidationErrorWhenPasswordConfirmIsEmpty() {
        //given
        User user = prepareUser();
        user.setPasswordConfirm("");

        //when
        var result = userValidator.validateForSave(user);

        //then
        assertThat(result.isError()).isTrue();
        assertThat(result.getDetail("passwordConfirm").getValidatorCode()).isEqualTo(ValidatorCode.IS_EMPTY);
        verify(userRepository, never()).save(user);
    }

    @Test
    public void shouldReturnValidationErrorWhenEmailIsNotValidCase1() {
        //given
        User user = prepareUser();
        user.setEmail("test@");

        //when
        var result = userValidator.validateForSave(user);

        //then
        assertThat(result.isError()).isTrue();
        assertThat(result.getDetail("email").getValidatorCode()).isEqualTo(ValidatorCode.IS_NOT_VALID);
        verify(userRepository, never()).save(user);
    }

    @Test
    public void shouldReturnValidationErrorWhenEmailIsNotValid_2() {
        //given
        User user = prepareUser();
        user.setEmail("test@test");

        //when
        var result = userValidator.validateForSave(user);

        //then
        assertThat(result.isError()).isTrue();
        assertThat(result.getDetail("email").getValidatorCode()).isEqualTo(ValidatorCode.IS_NOT_VALID);
        verify(userRepository, never()).save(user);
    }

    @Test
    public void shouldReturnValidationErrorWhenEmailIsNotValid_3() {
        //given
        User user = prepareUser();
        user.setEmail("testtest.pl");

        //when
        var result = userValidator.validateForSave(user);

        //then
        assertThat(result.isError()).isTrue();
        assertThat(result.getDetail("email").getValidatorCode()).isEqualTo(ValidatorCode.IS_NOT_VALID);
        verify(userRepository, never()).save(user);
    }

    @Test
    public void shouldReturnValidationErrorWhenEmailIsNotValid_4() {
        //given
        User user = prepareUser();
        user.setEmail("@test.pl");

        //when
        var result = userValidator.validateForSave(user);

        //then
        assertThat(result.isError()).isTrue();
        assertThat(result.getDetail("email").getValidatorCode()).isEqualTo(ValidatorCode.IS_NOT_VALID);
        verify(userRepository, never()).save(user);
    }


    @Test
    public void shouldReturnValidationErrorDuringUserEditionWhenUserWithSameLoginExists() {
        //given
        User user = prepareUser();
        user.setId(-1L);
        when(userRepository.countByLoginAndIdNot(any(String.class), any(Long.class))).thenReturn(1);

        //when
        var result = userValidator.validateForSave(user);

        //then
        assertThat(result.isError()).isTrue();
        assertThat(result.getDetail("login").getValidatorCode()).isEqualTo(ValidatorCode.ALREADY_EXISTS);
        verify(userRepository, never()).save(user);
    }

    @Test
    public void shouldReturnValidationErrorDuringUserEditionWhenUserWithSameEmailExists() {
        //given
        User user = prepareUser();
        user.setId(-1L);
        when(userRepository.countByEmailAndIdNot(any(String.class), any(Long.class))).thenReturn(1);

        //when
        var result = userValidator.validateForSave(user);

        //then
        assertThat(result.isError()).isTrue();
        assertThat(result.getDetail("email").getValidatorCode()).isEqualTo(ValidatorCode.ALREADY_EXISTS);
        verify(userRepository, never()).save(user);
    }

    @Test
    public void shouldReturnValidationErrorDuringUserCreationWhenUserWithSameEmailExists() {
        //given
        User user = prepareUser();
        when(userRepository.countByEmail(any(String.class))).thenReturn(1);

        //when
        var result = userValidator.validateForSave(user);

        //then
        assertThat(result.isError()).isTrue();
        assertThat(result.getDetail("email").getValidatorCode()).isEqualTo(ValidatorCode.ALREADY_EXISTS);
        verify(userRepository, never()).save(user);
    }


    @Test
    public void shouldReturnValidationErrorDuringUserCreationWhenUserWithSameLoginExists() {
        //given
        User user = prepareUser();
        when(userRepository.countByLogin(any(String.class))).thenReturn(1);

        //when
        var result = userValidator.validateForSave(user);

        //then
        assertThat(result.isError()).isTrue();
        assertThat(result.getDetail("login").getValidatorCode()).isEqualTo(ValidatorCode.ALREADY_EXISTS);
        verify(userRepository, never()).save(user);
    }

    @Test
    public void shouldReturnValidationErrorWhenPasswordAndPasswordConfirmAreNotSame() {
        //given
        User user = prepareUser();
        user.setPasswordConfirm("OTHERPASS");

        //when
        var result = userValidator.validateForSave(user);

        //then
        assertThat(result.isError()).isTrue();
        assertThat(result.getDetail("password").getValidatorCode()).isEqualTo(ValidatorCode.IS_NOT_SAME);
        verify(userRepository, never()).save(user);
    }


    private User prepareUser() {
        return User.builder().login("User").password("password").passwordConfirm("password").email("usert@mail.com").active(true).role(new Role()).build();
    }
}
