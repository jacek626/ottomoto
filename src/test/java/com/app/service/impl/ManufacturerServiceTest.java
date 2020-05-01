package com.app.service.impl;

import com.app.entity.Manufacturer;
import com.app.entity.VehicleModel;
import com.app.enums.ValidatorCode;
import com.app.enums.VehicleType;
import com.app.repository.AnnouncementRepository;
import com.app.repository.ManufacturerRepository;
import com.app.repository.VehicleModelRepository;
import com.app.utils.Result;
import com.app.validator.ManufacturerValidator;
import com.app.validator.VehicleModelValidator;
import com.querydsl.core.types.Predicate;
import org.assertj.core.util.Lists;
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
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ManufacturerServiceTest {

	private static Validator validator;

	@Mock
	private ManufacturerRepository manufacturerRepository;

	@Mock
	private AnnouncementRepository announcementRepository;

	@Mock
	private VehicleModelRepository vehicleModelRepository;

	@InjectMocks
	private VehicleModelValidator vehicleModelValidator = spy(VehicleModelValidator.class);

	@InjectMocks
	private ManufacturerValidator manufacturerValidator = spy(ManufacturerValidator.class);

	@InjectMocks
	private ManufacturerServiceImpl manufacturerService;

	@BeforeAll
	public static void init() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	public void shouldSaveManufacturer() {
		//given
		Manufacturer manufacturer = new Manufacturer("Manufacturer");
		manufacturer.setId(-1L);

		//when
		Result result = manufacturerService.saveManufacturer(manufacturer);
		Set<ConstraintViolation<Manufacturer>> manufacturerEntityValidation = validator.validate( manufacturer );

		//then
		assertThat(manufacturerEntityValidation.size()).isZero();
		assertThat(result.isSuccess()).isTrue();
		verify(manufacturerRepository, times(1)).save(any(Manufacturer.class));
	}
	
	@Test
	public void shouldSaveManufacturerWithVehicleModels() {
		//given
		Manufacturer manufacturer = new Manufacturer("Manufacturer");
		manufacturer.setVehicleModel(Lists.list(
				new VehicleModel("VehicleModel1", manufacturer, VehicleType.CAR),
				new VehicleModel("VehicleModel2", manufacturer, VehicleType.CAR)));

		//when
		Result result = manufacturerService.saveManufacturer(manufacturer);

		//then
		assertThat(result.isSuccess()).isTrue();
		verify(manufacturerRepository, times(1)).save(any(Manufacturer.class));
	}
	
	@Test
	public void shouldReturnValidationErrorDuringCreateNewManufacturerBecElementWithSameNameExists() {
		//given
		Manufacturer manufacturer = new Manufacturer("Manufacturer");
		when(manufacturerRepository.findByName(anyString())).thenReturn(List.of(new Manufacturer("Manufacturer")));

		//when
		Result result = manufacturerService.saveManufacturer(manufacturer);

		//then
		assertThat(result.isError()).isTrue();
		assertThat(result.getDetail("name").getCode()).isEqualTo(ValidatorCode.ALREADY_EXISTS);
	}

	@Test
	public void shouldReturnValidationErrorDuringEditManufacturerBecElementWithSameNameExists() {
		//given
		Manufacturer manufacturer = new Manufacturer("Manufacturer");
		manufacturer.setId(-2L);
		when(manufacturerRepository.findByName(anyString())).thenReturn(List.of(new Manufacturer(-1L,"Manufacturer")));

		//when
		Result result = manufacturerService.saveManufacturer(manufacturer);

		//then
		assertThat(result.isError()).isTrue();
		assertThat(result.getDetail("name").getCode()).isEqualTo(ValidatorCode.ALREADY_EXISTS);
	}

	@Test
	public void shouldEditManufacturerNameIsNotChanged() {
		//given
		Manufacturer manufacturer = new Manufacturer("Manufacturer");
		manufacturer.setId(-2L);
		when(manufacturerRepository.findByName(anyString())).thenReturn(List.of(new Manufacturer(-2L, "Manufacturer")));

		//when
		Result result = manufacturerService.saveManufacturer(manufacturer);

		//then
		assertThat(result.isSuccess()).isTrue();
		verify(manufacturerRepository, times(1)).save(any(Manufacturer.class));
	}

	@Test
	public void shouldEditManufacturerNameIsChanged() {
		//given
		Manufacturer manufacturer = new Manufacturer("Manufacturer");
		manufacturer.setId(-2L);
		when(manufacturerRepository.findByName(anyString())).thenReturn(List.of(new Manufacturer(-2L,"Manufacturer Test")));

		//when
		Result result = manufacturerService.saveManufacturer(manufacturer);

		//then
		assertThat(result.isSuccess()).isTrue();
		verify(manufacturerRepository, times(1)).save(refEq(new Manufacturer(-2L, "Manufacturer")));
	}
	
	@Test
	public void shouldDeleteManufacturer() {
		//given
		Manufacturer manufacturer = new Manufacturer("Manufacturer");

		//when
		Result saveResult = manufacturerService.deleteManufacturer(manufacturer);

		//then
		assertThat(saveResult.isSuccess()).isTrue();
		verify(manufacturerRepository, times(1)).delete(manufacturer);
	}
	
	@Test
	public void shouldReturnValidationErrorBecManufacturerHaveVehicleModels() {
		//given
		Manufacturer manufacturer = new Manufacturer("Manufacturer");
		manufacturer.setVehicleModel(Lists.list(new VehicleModel(-1L,"Vehicle1", manufacturer, VehicleType.CAR), new VehicleModel(1L,"Vehicle2", manufacturer, VehicleType.CAR)));
		when(announcementRepository.countByPredicates(any(Predicate.class))).thenReturn(1L);

		//when
		Result saveResult = manufacturerService.deleteManufacturer(manufacturer);

		//then
		assertThat(saveResult.isError()).isTrue();
	}
	
}
