package com.app.service.impl;

import com.app.entity.Announcement;
import com.app.entity.Picture;
import com.app.enums.ValidatorCode;
import com.app.repository.AnnouncementRepository;
import com.app.utils.Result;
import com.app.utils.TestUtils;
import com.app.validator.AnnouncementValidator;
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
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AnnouncementServiceTest {

	private static Validator validator;

	@Mock
	private AnnouncementRepository announcementRepository;

	@InjectMocks
    @SuppressWarnings("unused")
    private final AnnouncementValidator announcementValidator = spy(AnnouncementValidator.class);

	@InjectMocks
	private AnnouncementServiceImpl announcementService;

	@BeforeAll
	public static void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	public void shouldSaveAnnouncement() {
        //given
        Announcement announcement = TestUtils.prepareAnnouncement();

        //when
        Result result = announcementService.saveAnnouncement(announcement);
        Set<ConstraintViolation<Announcement>> announcementEntityValidation = validator.validate(announcement);

        //then
        assertThat(announcementEntityValidation.size()).isZero();
        assertThat(result.isSuccess()).isTrue();
        verify(announcementRepository, times(1)).save(any(Announcement.class));
    }

	@Test
	public void shouldSaveAnnouncementWithPictures() {
        //given
        Announcement announcement = TestUtils.prepareAnnouncement();
        List<Picture> pictures = List.of(Picture.builder().fileName("test").announcement(announcement).repositoryName("test").build(),
                Picture.builder().fileName("test2").announcement(announcement).pictureToDelete(true).repositoryName("test2").build());
        announcement.setPictures(pictures);

        //when
        Result result = announcementService.saveAnnouncement(announcement);
        Set<ConstraintViolation<Announcement>> announcementEntityValidation = validator.validate(announcement);

        //then
        assertThat(announcementEntityValidation.size()).isZero();
        assertThat(result.isSuccess()).isTrue();
        verify(announcementRepository, times(1)).save(any(Announcement.class));
    }
	
	@Test
	public void shouldReturnErrorBecUserIsNull() {
        //given
        Announcement announcement = TestUtils.prepareAnnouncement();
        announcement.setUser(null);

        //when
        Result result = announcementService.saveAnnouncement(announcement);
        Set<ConstraintViolation<Announcement>> announcementEntityValidation = validator.validate(announcement);

        //then
        assertThat(announcementEntityValidation.size()).isOne();
        assertThat(result.isError()).isTrue();
        assertThat(result.getDetail("user").getCode()).isEqualTo(ValidatorCode.IS_EMPTY);
	}
	
	@Test
	public void shouldReturnValidationErrorBecausePriceIsBelowZero() {
        //given
        Announcement announcement = TestUtils.prepareAnnouncement();
        announcement.setPrice(BigDecimal.valueOf(-100));

        //when
        Result result = announcementService.saveAnnouncement(announcement);

        //then
        assertThat(result.isError()).isTrue();
        assertThat(result.getDetail("price").getCode()).isEqualTo(ValidatorCode.IS_NEGATIVE);
    }
	
	@Test
	public void shouldReturnValidationErrorBecauseUserIsDeactivated() {
        //given
        Announcement announcement = TestUtils.prepareAnnouncement();
        announcement.getUser().setActive(false);

        //when
        Result result = announcementService.saveAnnouncement(announcement);

        //then
        assertThat(result.isError()).isTrue();
        assertThat(result.getValidationResult().get("user").getCode()).isEqualTo(ValidatorCode.IS_DEACTIVATED);
    }
	
	@Test
	public void shouldDeactivateAnnouncement() {
        //given
        Announcement announcement = TestUtils.prepareAnnouncement();
        announcement.setId(-999L);
        when(announcementRepository.existsByIdAndDeactivationDateIsNull(any(Long.class))).thenReturn(true);

        //when
        Result deactivationResult = announcementService.deactivateAnnouncement(announcement.getId());

        //then
        assertThat(deactivationResult.isSuccess()).isTrue();
        verify(announcementRepository, times(1)).updateDeactivationDateByAnnouncementId(any(Date.class), any(Long.class));

	}


}
