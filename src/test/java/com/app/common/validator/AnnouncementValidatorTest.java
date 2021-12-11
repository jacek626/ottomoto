package com.app.common.validator;

import com.app.announcement.entity.Announcement;
import com.app.announcement.validator.AnnouncementValidator;
import com.app.common.types.ValidatorCode;
import com.app.common.utils.TestUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    private AnnouncementValidator announcementValidator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    public void beforeEach() {
        announcementValidator = new AnnouncementValidator();
    }

    @Test
    public void shouldValidateAnnouncement() {
        //given
        var announcement = TestUtils.prepareAnnouncement();

        //when
        var result = announcementValidator.validateForSave(announcement);
        Set<ConstraintViolation<Announcement>> announcementEntityValidation = validator.validate(announcement);

        //then
        assertThat(result.isSuccess()).isTrue();
        assertThat(announcementEntityValidation.size()).isZero();
    }

    @Test
    public void shouldReturnValidationErrorWhenMillageIsNull() {
        //given
        var announcement = TestUtils.prepareAnnouncement();
        announcement.setMileage(null);

        //when
        var result = announcementValidator.validateForSave(announcement);
        Set<ConstraintViolation<Announcement>> announcementEntityValidation = validator.validate(announcement);

        //then
        assertThat(result.isSuccess()).isTrue();
        assertThat(announcementEntityValidation.size()).isOne();
    }

    @Test
    public void shouldReturnValidationErrorWhenEnginePowerIsNull() {
        //given
        var announcement = TestUtils.prepareAnnouncement();
        announcement.setEnginePower(null);

        //when
        var result = announcementValidator.validateForSave(announcement);
        Set<ConstraintViolation<Announcement>> announcementEntityValidation = validator.validate(announcement);

        //then
        assertThat(result.isSuccess()).isTrue();
        assertThat(announcementEntityValidation.size()).isOne();
    }

    @Test
    public void shouldReturnValidationErrorWhenPriceIsNull() {
        //given
        var announcement = TestUtils.prepareAnnouncement();
        announcement.setPrice(null);

        //when
        var result = announcementValidator.validateForSave(announcement);
        Set<ConstraintViolation<Announcement>> announcementEntityValidation = validator.validate(announcement);

        //then
        assertThat(result.isError()).isTrue();
        assertThat(announcementEntityValidation.size()).isOne();
    }

    @Test
    public void shouldReturnValidationErrorWhenPriceIsBelowZero() {
        //given
        var announcement = TestUtils.prepareAnnouncement();
        announcement.setPrice(BigDecimal.valueOf(-1));

        //when
        var result = announcementValidator.validateForSave(announcement);
        Set<ConstraintViolation<Announcement>> announcementEntityValidation = validator.validate(announcement);

        //then
        assertThat(result.isError()).isTrue();
        assertThat(announcementEntityValidation.size()).isOne();
    }

    @Test
    public void shouldReturnValidationErrorWhenUserIsNull() {
        //given
        var announcement = TestUtils.prepareAnnouncement();
        announcement.setUser(null);

        //when
        var result = announcementValidator.validateForSave(announcement);
        Set<ConstraintViolation<Announcement>> announcementEntityValidation = validator.validate(announcement);

        //then
        assertThat(announcementEntityValidation.size()).isOne();
        assertThat(result.isError()).isTrue();
        assertThat(result.getDetail("user").getValidatorCode()).isEqualTo(ValidatorCode.IS_EMPTY);
    }

    @Test
    public void shouldReturnValidationErrorWhenPriceIsBelowZero_2() {
        //given
        var announcement = TestUtils.prepareAnnouncement();
        announcement.setPrice(BigDecimal.valueOf(-100));

        //when
        var result = announcementValidator.validateForSave(announcement);

        //then
        assertThat(result.isError()).isTrue();
        assertThat(result.getDetail("price").getValidatorCode()).isEqualTo(ValidatorCode.IS_NEGATIVE);
    }

    @Test
    public void shouldReturnValidationErrorWhenUserIsDeactivated() {
        //given
        var announcement = TestUtils.prepareAnnouncement();
        announcement.getUser().setActive(false);

        //when
        var result = announcementValidator.validateForSave(announcement);

        //then
        assertThat(result.isError()).isTrue();
        assertThat(result.getValidationResult().get("user").getValidatorCode()).isEqualTo(ValidatorCode.IS_DEACTIVATED);
    }

}
