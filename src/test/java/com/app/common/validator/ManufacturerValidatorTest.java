package com.app.common.validator;

import com.app.announcement.repository.AnnouncementRepository;
import com.app.common.enums.ValidatorCode;
import com.app.common.enums.VehicleType;
import com.app.common.utils.validation.Result;
import com.app.manufacturer.entity.Manufacturer;
import com.app.manufacturer.repository.ManufacturerRepository;
import com.app.manufacturer.validator.ManufacturerValidator;
import com.app.vehiclemodel.entity.VehicleModel;
import com.app.vehiclemodel.validator.VehicleModelValidator;
import com.querydsl.core.types.Predicate;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ManufacturerValidatorTest {

    private static Validator validator;

    @Mock
    private ManufacturerRepository manufacturerRepository;

    @Mock
    private AnnouncementRepository announcementRepository;

    @Mock
    private VehicleModelValidator vehicleModelValidator;

    @InjectMocks
    private ManufacturerValidator manufacturerValidator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    @Test
    public void shouldReturnValidationErrorDuringCreateNewManufacturerBecElementWithSameNameExists() {
        //given
        Manufacturer manufacturer = new Manufacturer("Manufacturer");
        when(manufacturerRepository.findByName(anyString())).thenReturn(List.of(new Manufacturer("Manufacturer")));

        //when
        Result result = manufacturerValidator.checkBeforeSave(manufacturer);

        //then
        assertThat(result.isError()).isTrue();
        assertThat(result.getDetail("name").getValidatorCode()).isEqualTo(ValidatorCode.ALREADY_EXISTS);
    }


    @Test
    public void shouldReturnValidationErrorDuringEditManufacturerBecElementWithSameNameExists() {
        //given
        Manufacturer manufacturer = new Manufacturer("Manufacturer");
        manufacturer.setId(-2L);
        when(manufacturerRepository.findByName(anyString())).thenReturn(List.of(new Manufacturer(-1L, "Manufacturer")));

        //when
        Result result = manufacturerValidator.checkBeforeSave(manufacturer);

        //then
        assertThat(result.isError()).isTrue();
        assertThat(result.getDetail("name").getValidatorCode()).isEqualTo(ValidatorCode.ALREADY_EXISTS);
    }

    @Test
    public void shouldReturnValidationErrorBecManufacturerHaveVehicleModels() {
        //given
        Manufacturer manufacturer = new Manufacturer("Manufacturer");
        manufacturer.setVehicleModel(Lists.list(
                VehicleModel.builder().id(-1L).name("Vehicle1").manufacturer(manufacturer).vehicleType(VehicleType.CAR).build(),
                VehicleModel.builder().id(-1L).name("Vehicle2").manufacturer(manufacturer).vehicleType(VehicleType.CAR).build()));
        when(announcementRepository.countByPredicates(any(Predicate.class))).thenReturn(1L);
        when(vehicleModelValidator.checkBeforeDelete(any(VehicleModel.class))).thenReturn(Result.error());

        //when
        Result saveResult = manufacturerValidator.checkBeforeDelete(manufacturer);

        //then
        assertThat(saveResult.isError()).isTrue();
    }

}
