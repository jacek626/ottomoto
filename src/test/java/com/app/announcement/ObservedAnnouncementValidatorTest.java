package com.app.announcement;

import com.app.announcement.entity.Announcement;
import com.app.announcement.entity.ObservedAnnouncement;
import com.app.announcement.validator.ObservedAnnouncementValidator;
import com.app.common.types.ValidatorCode;
import com.app.common.utils.validation.ValidationDetails;
import com.app.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ObservedAnnouncementValidatorTest {

    private final ObservedAnnouncementValidator observedAnnouncementValidator = new ObservedAnnouncementValidator();

    @Test
    public void shouldVerifyObservedAnnouncement() {
        //given
        ObservedAnnouncement observedAnnouncement =
                ObservedAnnouncement.builder().announcement(new Announcement()).user(new User()).build();

        //when
        var result = observedAnnouncementValidator.validateForSave(observedAnnouncement);

        //then
        assertThat(result.isSuccess()).isTrue();
    }

    @Test
    public void shouldReturnValidationErrorWhenUserAndAnnouncementAreNull() {
        //given
        ObservedAnnouncement observedAnnouncement =
                ObservedAnnouncement.builder().announcement(null).user(null).build();

        //when
        var result = observedAnnouncementValidator.validateForSave(observedAnnouncement);

        //then
        assertThat(result.isError()).isTrue();
        assertThat(result.getValidationResult().get("user")).isEqualTo(ValidationDetails.of(ValidatorCode.IS_EMPTY));
        assertThat(result.getValidationResult().get("announcement")).isEqualTo(ValidationDetails.of(ValidatorCode.IS_EMPTY));
    }

    @Test
    public void shouldReturnValidationErrorWhenUserAndAnnouncementAreNotActive() {
        //given
        ObservedAnnouncement observedAnnouncement = ObservedAnnouncement.builder()
                        .announcement(Announcement.builder().active(false).build())
                        .user(User.builder().active(false).build())
                        .build();

        //when
        var result = observedAnnouncementValidator.validateForSave(observedAnnouncement);

        //then
        assertThat(result.isError()).isTrue();
        assertThat(result.getValidationResult().get("user")).isEqualTo(ValidationDetails.of(ValidatorCode.IS_DEACTIVATED));
        assertThat(result.getValidationResult().get("announcement")).isEqualTo(ValidationDetails.of(ValidatorCode.IS_DEACTIVATED));
    }
}
