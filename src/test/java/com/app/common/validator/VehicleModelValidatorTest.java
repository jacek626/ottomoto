package com.app.common.validator;

import com.app.announcement.repository.AnnouncementRepository;
import com.app.common.enums.ValidatorCode;
import com.app.common.utils.validation.Result;
import com.app.vehiclemodel.entity.VehicleModel;
import com.app.vehiclemodel.repository.VehicleModelRepository;
import com.app.vehiclemodel.validator.VehicleModelValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class VehicleModelValidatorTest {

    @Mock
    private VehicleModelRepository vehicleModelRepository;

    @Mock
    private AnnouncementRepository announcementRepository;

    @InjectMocks
    private VehicleModelValidator vehicleModelValidator;

    @Test
    public void shouldVerifyVehicleModelAsCorrectBeforeSave() {
        //given
        VehicleModel vehicleModel = VehicleModel.builder().name("testName").build();

        //when
        Result result = vehicleModelValidator.checkBeforeSave(vehicleModel);

        //then
        assertThat(result.isSuccess()).isTrue();
    }

    @Test
    public void shouldReturnErrorBecNameIsBlank() {
        //given
        VehicleModel vehicleModel = VehicleModel.builder().name("").build();

        //when
        Result result = vehicleModelValidator.checkBeforeSave(vehicleModel);

        //then
        assertThat(result.getValidationResult()).containsKey("VehicleModelName");
        assertThat(result.isError()).isTrue();
    }

    @Test
    public void shouldReturnErrorBecNameIsNull() {
        //given
        VehicleModel vehicleModel = VehicleModel.builder().name(null).build();

        //when
        Result result = vehicleModelValidator.checkBeforeSave(vehicleModel);

        //then
        assertThat(result.getValidationResult()).containsKey("VehicleModelName");
        assertThat(result.isError()).isTrue();
    }

    @Test
    public void shouldReturnErrorBecNameIsNotUnique_ScenarioForNewObject() {
        //given
        VehicleModel vehicleModel = VehicleModel.builder().name("notUniqueName").build();
        when(vehicleModelRepository.countByName(any(String.class))).thenReturn(1);

        //when
        Result result = vehicleModelValidator.checkBeforeSave(vehicleModel);

        //then
        assertThat(result.getValidationResult().get("VehicleModelName").getValidatorCode()).isEqualTo(ValidatorCode.ALREADY_EXISTS);
        assertThat(result.isError()).isTrue();
    }

    @Test
    public void shouldReturnErrorBecNameIsNotUnique_ScenarioForExistingObject() {
        //given
        VehicleModel vehicleModel = VehicleModel.builder().id(1L).name("notUniqueName").build();
        when(vehicleModelRepository.countByNameAndIdNot(any(String.class), any(Long.class))).thenReturn(1);

        //when
        Result result = vehicleModelValidator.checkBeforeSave(vehicleModel);

        //then
        assertThat(result.getValidationResult().get("VehicleModelName").getValidatorCode()).isEqualTo(ValidatorCode.ALREADY_EXISTS);
        assertThat(result.isError()).isTrue();
    }

    @Test
    public void shouldVerifyVehicleModelAsCorrectBeforeDelete() {
        //given
        VehicleModel vehicleModel = VehicleModel.builder().name("testName").build();
        when(announcementRepository.existsByVehicleModel(any(VehicleModel.class))).thenReturn(false);

        //when
        Result result = vehicleModelValidator.checkBeforeDelete(vehicleModel);

        //then
        assertThat(result.isSuccess()).isTrue();
    }

    @Test
    public void shouldReturnErrorBecAnnouncementsWithThisVehicleModelExists() {
        //given
        VehicleModel vehicleModel = VehicleModel.builder().id(1L).name("testName").build();
        when(announcementRepository.existsByVehicleModel(any(VehicleModel.class))).thenReturn(true);

        //when
        Result result = vehicleModelValidator.checkBeforeDelete(vehicleModel);

        //then
        assertThat(result.getValidationResult().get("announcements").getValidatorCode()).isEqualTo(ValidatorCode.HAVE_REF_OBJECTS);
        assertThat(result.isError()).isTrue();
    }


}
