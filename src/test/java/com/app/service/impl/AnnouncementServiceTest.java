package com.app.service.impl;

import com.app.entity.Announcement;
import com.app.entity.Manufacturer;
import com.app.entity.User;
import com.app.entity.VehicleModel;
import com.app.enums.ValidatorCode;
import com.app.enums.VehicleSubtype;
import com.app.enums.VehicleType;
import com.app.repository.AnnouncementRepository;
import com.app.utils.Result;
import com.app.validator.AnnouncementValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.Date;
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
	private AnnouncementValidator announcementValidator = spy(AnnouncementValidator.class);

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
		Announcement announcement  = prepareAnnouncementWithAllNeededObjects();

		//when
		Result result = announcementService.saveAnnouncement(announcement);
		Set<ConstraintViolation<Announcement>> announcementEntityValidation = validator.validate( announcement );

		//then
		assertThat(announcementEntityValidation.size()).isZero();
		assertThat(result.isSuccess()).isTrue();
		verify(announcementRepository, times(1)).save(Mockito.any(Announcement.class));
	}
	
	@Test
	public void shouldReturnValidationErrorBecouseUserIsNotDefined() {
		//given
		Announcement announcement  = prepareAnnouncementWithAllNeededObjects();
		announcement.setUser(null);

		//when
		Result result = announcementService.saveAnnouncement(announcement);
		Set<ConstraintViolation<Announcement>> announcementEntityValidation = validator.validate( announcement );

		//then
		assertThat(result.isError()).isTrue();
		assertThat(result.getValidationResult().get("user")).isEqualTo( ValidatorCode.IS_EMPTY);
	}
	
	@Test
	public void shouldReturnValidationErrorBecausePriceIsBelowZero() {
		//given
		Announcement announcement  = prepareAnnouncementWithAllNeededObjects();
		announcement.setPrice(BigDecimal.valueOf(-100));

		//when
		Result result = announcementService.saveAnnouncement(announcement);

		//then
		assertThat(result.isError()).isTrue();
		assertThat(result.getValidationResult().get("price")).isEqualTo( ValidatorCode.IS_NEGATIVE);
	}
	
	@Test
	public void shouldReturnValidationErrorBecauseUserIsDeactivated() {
		//given
		Announcement announcement  = prepareAnnouncementWithAllNeededObjects();
		announcement.getUser().setActive(false);

		//when
		Result result = announcementService.saveAnnouncement(announcement);

		//then
		assertThat(result.isError()).isTrue();
		assertThat(result.getValidationResult().get("user")).isEqualTo( ValidatorCode.IS_DEACTIVATED);
	}
	
	@Test
	public void shouldDeactivateAnnouncement() {
		//given
		Announcement announcement  = prepareAnnouncementWithAllNeededObjects();
		announcement.setId(-999L);
		when(announcementRepository.existsByIdAndDeactivationDateIsNull(any(Long.class))).thenReturn(true);

		//when
		Result deactivationResult = announcementService.deactivateAnnouncement(announcement.getId());

		//then
		assertThat(deactivationResult.isSuccess()).isTrue();
		verify(announcementRepository, times(1)).updateDeactivationDateByAnnouncementId(any(Date.class), any(Long.class));

	}

	private Announcement prepareAnnouncementWithAllNeededObjects() {
		User user = new User.UserBuilder("user","testPass","testPass", "user@test.com", true).build();
		Manufacturer manufacturer = new Manufacturer("manufacturer");
		manufacturer.getVehicleModel().add(new VehicleModel("vehicleModel" , manufacturer , VehicleType.CAR));

		Announcement announcement  = new Announcement.AnnouncementBuilder(manufacturer.getVehicleModel().get(0), VehicleSubtype.COMPACT, 2_000, 180_000, new BigDecimal(30_000), user).build();

		return announcement;
	}

	public void shouldDeleteObservedAnnouncement() {

	}
	
}
