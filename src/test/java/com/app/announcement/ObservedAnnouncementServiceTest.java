package com.app.announcement;

import com.app.announcement.entity.Announcement;
import com.app.announcement.entity.ObservedAnnouncement;
import com.app.announcement.types.VehicleSubtype;
import com.app.announcement.validator.ObservedAnnouncementValidator;
import com.app.common.utils.validation.Result;
import com.app.user.entity.User;
import com.app.vehiclemodel.entity.VehicleModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ObservedAnnouncementServiceTest {

    @Mock
    private ObservedAnnouncementRepository observedAnnouncementRepository;

    @Mock
    private ObservedAnnouncementValidator observedAnnouncementValidator;

    @InjectMocks
    private ObservedAnnouncementService observedAnnouncementService;

    @Test
    public void shouldSaveObservedAnnouncement() {
        //given
        var announcement = Announcement.builder().user(new User()).vehicleModel(new VehicleModel()).vehicleSubtype(VehicleSubtype.COMPACT).productionYear(2_000).price(BigDecimal.valueOf(180_000)).build();
        var user = User.builder().login("userLoginTest6").password("testPass").passwordConfirm("testPass").email("mailTest6@test.com").active(true).build();
        var observedAnnouncement = new ObservedAnnouncement(announcement, user);
        when(observedAnnouncementValidator.validateForSave(any(ObservedAnnouncement.class))).thenReturn(Result.success());

        //when
        var result = observedAnnouncementService.saveObservedAnnouncement(observedAnnouncement);

        //then
        assertThat(result.isSuccess()).isTrue();
        verify(observedAnnouncementRepository, times(1)).save(observedAnnouncement);
    }

    @Test
    public void shouldDeleteObservedAnnouncement() {
        //given
        var announcement = Announcement.builder().user(new User()).vehicleModel(new VehicleModel()).vehicleSubtype(VehicleSubtype.COMPACT).productionYear(2_000).price(BigDecimal.valueOf(180_000)).build();
        var user = User.builder().login("userLoginTest6").password("testPass").passwordConfirm("testPass").email("mailTest6@test.com").active(true).build();
        var observedAnnouncement = new ObservedAnnouncement(announcement, user);
        observedAnnouncement.setId(-2L);
        given(observedAnnouncementRepository.findById(any(Long.class))).willReturn(Optional.of(new ObservedAnnouncement(announcement, user)));

        //when
        var result = observedAnnouncementService.deleteObservedAnnouncement(observedAnnouncement);

        //then
        assertThat(result.isSuccess()).isTrue();
        verify(observedAnnouncementRepository, times(1)).delete(any(ObservedAnnouncement.class));
    }
}
