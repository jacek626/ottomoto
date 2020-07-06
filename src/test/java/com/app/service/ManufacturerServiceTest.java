package com.app.service;

import com.app.entity.Manufacturer;
import com.app.entity.VehicleModel;
import com.app.enums.VehicleType;
import com.app.repository.ManufacturerRepository;
import com.app.utils.validation.Result;
import com.app.validator.ManufacturerValidator;
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
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ManufacturerServiceTest {

    private static Validator validator;

    @Mock
    private ManufacturerRepository manufacturerRepository;

    @Mock
    private ManufacturerValidator manufacturerValidator;

    @InjectMocks
    private ManufacturerService manufacturerService;

    @BeforeAll
    public static void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

	@Test
	public void shouldSaveManufacturer() {
        //given
        Manufacturer manufacturer = Manufacturer.builder().name("Manufacturer").build();
        manufacturer.setId(-1L);
        when(manufacturerValidator.checkBeforeSave(any(Manufacturer.class))).thenReturn(Result.success());

        //when
        Result result = manufacturerService.saveManufacturer(manufacturer);
        Set<ConstraintViolation<Manufacturer>> manufacturerEntityValidation = validator.validate(manufacturer);

        //then
        assertThat(manufacturerEntityValidation.size()).isZero();
        assertThat(result.isSuccess()).isTrue();
        verify(manufacturerRepository, times(1)).save(any(Manufacturer.class));
    }

    @Test
    public void checkManufacturerValidatorRunBeforeSave() {
        //given
        Manufacturer manufacturer = Manufacturer.builder().name("Manufacturer").build();
        manufacturer.setId(-1L);
        when(manufacturerValidator.checkBeforeSave(any(Manufacturer.class))).thenReturn(Result.success());

        //when
        manufacturerService.saveManufacturer(manufacturer);

        //then
        verify(manufacturerValidator, times(1)).checkBeforeSave(any(Manufacturer.class));
    }

    @Test
    public void shouldSaveManufacturerWithVehicleModels() {
        //given
        Manufacturer manufacturer = new Manufacturer("Manufacturer");
        manufacturer.setVehicleModel(Lists.list(
                VehicleModel.builder().name("VehicleModel1").manufacturer(manufacturer).vehicleType(VehicleType.CAR).build(),
                VehicleModel.builder().name("VehicleModel2").manufacturer(manufacturer).vehicleType(VehicleType.CAR).build()));
        when(manufacturerValidator.checkBeforeSave(any(Manufacturer.class))).thenReturn(Result.success());

		//when
		Result result = manufacturerService.saveManufacturer(manufacturer);

		//then
        assertThat(result.isSuccess()).isTrue();
        verify(manufacturerRepository, times(1)).save(any(Manufacturer.class));
	}

    @Test
    public void shouldDeleteManufacturer() {
        //given
        Manufacturer manufacturer = Manufacturer.builder().name("Manufacturer").build();
        when(manufacturerValidator.checkBeforeDelete(any(Manufacturer.class))).thenReturn(Result.success());

        //when
        Result saveResult = manufacturerService.deleteManufacturer(manufacturer);

        //then
        assertThat(saveResult.isSuccess()).isTrue();
        verify(manufacturerRepository, times(1)).delete(manufacturer);
    }

    @Test
    public void checkIsManufacturerValidatorRunBeforeDelete() {
        //given
        Manufacturer manufacturer = Manufacturer.builder().name("Manufacturer").build();
        manufacturer.setId(-1L);
        when(manufacturerValidator.checkBeforeDelete(any(Manufacturer.class))).thenReturn(Result.success());

        //when
        manufacturerService.deleteManufacturer(manufacturer);

        //then
        verify(manufacturerValidator, times(1)).checkBeforeDelete(any(Manufacturer.class));
    }

    @Test
    public void shouldChangeManufacturerName() {
        //given
        Manufacturer baseManufacturer = Manufacturer.builder().name("Manufacturer").id(-2L).build();
        Manufacturer manufacturerAfterEdit = Manufacturer.builder().name("Edited manufacturer").id(-2L).build();
        when(manufacturerRepository.findByName(anyString())).thenReturn(List.of(baseManufacturer));
        when(manufacturerValidator.checkBeforeSave(any(Manufacturer.class))).thenReturn(Result.success());

        //when
        Result result = manufacturerService.saveManufacturer(manufacturerAfterEdit);

        //then
        assertThat(result.isSuccess()).isTrue();
        verify(manufacturerRepository, times(1)).save(refEq(manufacturerAfterEdit));
    }
}
