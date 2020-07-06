package com.app.validator;

import com.app.entity.Announcement;
import com.app.enums.ValidatorCode;
import com.app.repository.AnnouncementRepository;
import com.app.utils.TestUtils;
import com.app.utils.validation.Result;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AnnouncementValidatorTest {

    private static Validator validator;

    @Mock
    private AnnouncementRepository announcementRepository;

    @InjectMocks
    private AnnouncementValidator announcementValidator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldValidateAnnouncementCorrectly() {
        //given
        Announcement announcement = TestUtils.prepareAnnouncement();

        //when
        Result result = announcementValidator.checkBeforeSave(announcement);
        Set<ConstraintViolation<Announcement>> announcementEntityValidation = validator.validate(announcement);

        //then
        assertThat(result.isSuccess()).isTrue();
        assertThat(announcementEntityValidation.size()).isZero();
    }

    @Test
    public void shouldReturnErrorBecMillageIsNull() {
        //given
        Announcement announcement = TestUtils.prepareAnnouncement();
        announcement.setMileage(null);

        //when
        Result result = announcementValidator.checkBeforeSave(announcement);
        Set<ConstraintViolation<Announcement>> announcementEntityValidation = validator.validate(announcement);

        //then
        assertThat(result.isSuccess()).isTrue();
        assertThat(announcementEntityValidation.size()).isOne();
    }

    @Test
    public void shouldReturnErrorBecEnginePowerIsNull() {
        //given
        Announcement announcement = TestUtils.prepareAnnouncement();
        announcement.setEnginePower(null);

        //when
        Result result = announcementValidator.checkBeforeSave(announcement);
        Set<ConstraintViolation<Announcement>> announcementEntityValidation = validator.validate(announcement);

        //then
        assertThat(result.isSuccess()).isTrue();
        assertThat(announcementEntityValidation.size()).isOne();
    }

    @Test
    public void shouldReturnErrorBecPriceIsNull() {
        //given
        Announcement announcement = TestUtils.prepareAnnouncement();
        announcement.setPrice(null);

        //when
        Result result = announcementValidator.checkBeforeSave(announcement);
        Set<ConstraintViolation<Announcement>> announcementEntityValidation = validator.validate(announcement);

        //then
        assertThat(result.isError()).isTrue();
        assertThat(announcementEntityValidation.size()).isOne();
    }

    @Test
    public void shouldReturnErrorBecPriceIsBelowZero() {
        //given
        Announcement announcement = TestUtils.prepareAnnouncement();
        announcement.setPrice(BigDecimal.valueOf(-1));

        //when
        Result result = announcementValidator.checkBeforeSave(announcement);
        Set<ConstraintViolation<Announcement>> announcementEntityValidation = validator.validate(announcement);

        //then
        assertThat(result.isError()).isTrue();
        assertThat(announcementEntityValidation.size()).isOne();
    }

    @Test
    public void shouldReturnErrorBecUserIsNull() {
        //given
        Announcement announcement = TestUtils.prepareAnnouncement();
        announcement.setUser(null);

        //when
        Result result = announcementValidator.checkBeforeSave(announcement);
        Set<ConstraintViolation<Announcement>> announcementEntityValidation = validator.validate(announcement);

        //then
        assertThat(announcementEntityValidation.size()).isOne();
        assertThat(result.isError()).isTrue();
        assertThat(result.getDetail("user").getValidatorCode()).isEqualTo(ValidatorCode.IS_EMPTY);
    }

    @Test
    public void shouldReturnErrorBecausePriceIsBelowZero() {
        //given
        Announcement announcement = TestUtils.prepareAnnouncement();
        announcement.setPrice(BigDecimal.valueOf(-100));

        //when
        Result result = announcementValidator.checkBeforeSave(announcement);

        //then
        assertThat(result.isError()).isTrue();
        assertThat(result.getDetail("price").getValidatorCode()).isEqualTo(ValidatorCode.IS_NEGATIVE);
    }

    @Test
    public void shouldReturnErrorBecauseUserIsDeactivated() {
        //given
        Announcement announcement = TestUtils.prepareAnnouncement();
        announcement.getUser().setActive(false);

        //when
        Result result = announcementValidator.checkBeforeSave(announcement);

        //then
        assertThat(result.isError()).isTrue();
        assertThat(result.getValidationResult().get("user").getValidatorCode()).isEqualTo(ValidatorCode.IS_DEACTIVATED);
    }

}
