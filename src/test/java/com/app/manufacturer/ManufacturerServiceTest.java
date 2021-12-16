package com.app.manufacturer;

import com.app.announcement.types.VehicleType;
import com.app.common.utils.validation.Result;
import com.app.manufacturer.entity.Manufacturer;
import com.app.manufacturer.repository.ManufacturerRepository;
import com.app.manufacturer.validator.ManufacturerValidator;
import com.app.vehiclemodel.entity.VehicleModel;
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
import static org.mockito.BDDMockito.given;
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
        given(manufacturerValidator.validateForSave(any(Manufacturer.class))).willReturn(Result.success());

        //when
        var result = manufacturerService.saveManufacturer(manufacturer);
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
        given(manufacturerValidator.validateForSave(any(Manufacturer.class))).willReturn(Result.success());

        //when
        manufacturerService.saveManufacturer(manufacturer);

        //then
        verify(manufacturerValidator, times(1)).validateForSave(any(Manufacturer.class));
    }

    @Test
    public void shouldSaveManufacturerWithVehicleModels() {
        //given
        Manufacturer manufacturer = new Manufacturer("Manufacturer");
        manufacturer.setVehicleModel(Lists.list(
                VehicleModel.builder().name("VehicleModel1").manufacturer(manufacturer).vehicleType(VehicleType.CAR).build(),
                VehicleModel.builder().name("VehicleModel2").manufacturer(manufacturer).vehicleType(VehicleType.CAR).build()));
        given(manufacturerValidator.validateForSave(any(Manufacturer.class))).willReturn(Result.success());

		//when
		var result = manufacturerService.saveManufacturer(manufacturer);

		//then
        assertThat(result.isSuccess()).isTrue();
        verify(manufacturerRepository, times(1)).save(any(Manufacturer.class));
	}

    @Test
    public void shouldDeleteManufacturer() {
        //given
        Manufacturer manufacturer = Manufacturer.builder().name("Manufacturer").build();
        given(manufacturerValidator.validateForDelete(any(Manufacturer.class))).willReturn(Result.success());

        //when
        var saveResult = manufacturerService.deleteManufacturer(manufacturer);

        //then
        assertThat(saveResult.isSuccess()).isTrue();
        verify(manufacturerRepository, times(1)).delete(manufacturer);
    }

    @Test
    public void checkIsManufacturerValidatorRunBeforeDelete() {
        //given
        Manufacturer manufacturer = Manufacturer.builder().name("Manufacturer").build();
        manufacturer.setId(-1L);
        given(manufacturerValidator.validateForDelete(any(Manufacturer.class))).willReturn(Result.success());

        //when
        manufacturerService.deleteManufacturer(manufacturer);

        //then
        verify(manufacturerValidator, times(1)).validateForDelete(any(Manufacturer.class));
    }

    @Test
    public void shouldChangeManufacturerName() {
        //given
        Manufacturer baseManufacturer = Manufacturer.builder().name("Manufacturer").id(-2L).build();
        Manufacturer manufacturerAfterEdit = Manufacturer.builder().name("Edited manufacturer").id(-2L).build();
        given(manufacturerRepository.findByName(anyString())).willReturn(List.of(baseManufacturer));
        given(manufacturerValidator.validateForSave(any(Manufacturer.class))).willReturn(Result.success());

        //when
        var result = manufacturerService.saveManufacturer(manufacturerAfterEdit);

        //then
        assertThat(result.isSuccess()).isTrue();
        verify(manufacturerRepository, times(1)).save(refEq(manufacturerAfterEdit));
    }
}
