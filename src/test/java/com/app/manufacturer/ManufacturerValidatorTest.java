package com.app.manufacturer;

import com.app.announcement.repository.AnnouncementRepository;
import com.app.announcement.types.VehicleType;
import com.app.common.types.ValidatorCode;
import com.app.common.utils.validation.Result;
import com.app.manufacturer.entity.Manufacturer;
import com.app.manufacturer.repository.ManufacturerRepository;
import com.app.manufacturer.validator.ManufacturerValidator;
import com.app.vehiclemodel.entity.VehicleModel;
import com.app.vehiclemodel.validator.VehicleModelValidator;
import com.querydsl.core.types.Predicate;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ManufacturerValidatorTest {
    @Mock
    private ManufacturerRepository manufacturerRepository;

    @Mock
    private AnnouncementRepository announcementRepository;

    @Mock
    private VehicleModelValidator vehicleModelValidator;

    @InjectMocks
    private ManufacturerValidator manufacturerValidator;

    @Test
    public void shouldReturnValidationErrorDuringCreateNewManufacturerBecElementWithSameNameExists() {
        //given
        var manufacturer = new Manufacturer("Manufacturer");
        given(manufacturerRepository.findByName(anyString())).willReturn(List.of(new Manufacturer("Manufacturer")));

        //when
        var result = manufacturerValidator.validateForSave(manufacturer);

        //then
        assertThat(result.isError()).isTrue();
        assertThat(result.getDetail("name").getValidatorCode()).isEqualTo(ValidatorCode.ALREADY_EXISTS);
    }


    @Test
    public void shouldReturnValidationErrorDuringEditManufacturerBecElementWithSameNameExists() {
        //given
        var manufacturer = new Manufacturer("Manufacturer");
        manufacturer.setId(-2L);
        given(manufacturerRepository.findByName(anyString())).willReturn(List.of(new Manufacturer(-1L, "Manufacturer")));

        //when
        var result = manufacturerValidator.validateForSave(manufacturer);

        //then
        assertThat(result.isError()).isTrue();
        assertThat(result.getDetail("name").getValidatorCode()).isEqualTo(ValidatorCode.ALREADY_EXISTS);
    }

    @Test
    public void shouldReturnValidationErrorBecManufacturerHaveVehicleModels() {
        //given
        var manufacturer = new Manufacturer("Manufacturer");
        manufacturer.setVehicleModel(Lists.list(
                VehicleModel.builder().id(-1L).name("Vehicle1").manufacturer(manufacturer).vehicleType(VehicleType.CAR).build(),
                VehicleModel.builder().id(-1L).name("Vehicle2").manufacturer(manufacturer).vehicleType(VehicleType.CAR).build()));
        given(announcementRepository.countByPredicates(any(Predicate.class))).willReturn(1L);
        given(vehicleModelValidator.validateForDelete(any(VehicleModel.class))).willReturn(Result.error());

        //when
        var saveResult = manufacturerValidator.validateForDelete(manufacturer);

        //then
        assertThat(saveResult.isError()).isTrue();
    }
}
