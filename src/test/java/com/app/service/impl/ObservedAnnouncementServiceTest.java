package com.app.service.impl;

import com.app.entity.Announcement;
import com.app.entity.ObservedAnnouncement;
import com.app.entity.User;
import com.app.enums.VehicleSubtype;
import com.app.repository.ObservedAnnouncementRepository;
import com.app.utils.Result;
import com.app.validator.ObservedAnnouncementValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ObservedAnnouncementServiceTest {

    @Mock
    private ObservedAnnouncementRepository observedAnnouncementRepository;

    @InjectMocks
    private ObservedAnnouncementValidator observedAnnouncementValidator = spy(ObservedAnnouncementValidator.class);

    @InjectMocks
    private ObservedAnnouncementServiceImpl observedAnnouncementService;

    @Test
    public void shouldSaveObservedAnnouncement() {
        //given
        Announcement announcement  = new Announcement.AnnouncementBuilder(null, VehicleSubtype.COMPACT, 2_000, 180_000, new BigDecimal(30_000), null).build();
        User user = new User.UserBuilder("userLoginTest6","testPass","testPass","mailTest6@test.com", true).build();
        ObservedAnnouncement observedAnnouncement = new ObservedAnnouncement(announcement, user);

        //when
        Result result = observedAnnouncementService.saveObservedAnnouncement(observedAnnouncement);

        //then
        assertThat(result.isSuccess()).isTrue();
        verify(observedAnnouncementRepository, times(1)).save(observedAnnouncement);
    }

    @Test
    public void shouldDeleteObservedAnnouncement() {
        //given
        Announcement announcement  = new Announcement.AnnouncementBuilder(null, VehicleSubtype.COMPACT, 2_000, 180_000, new BigDecimal(30_000), null).build();
        User user = new User.UserBuilder("userLoginTest6","testPass","testPass","mailTest6@test.com", true).build();
        ObservedAnnouncement observedAnnouncement = new ObservedAnnouncement(announcement, user);
        observedAnnouncement.setId(-2L);
        when(observedAnnouncementRepository.findById(any(Long.class))).thenReturn(Optional.of(new ObservedAnnouncement(announcement, user)));

        //when
        Result result = observedAnnouncementService.deleteObservedAnnouncement(observedAnnouncement);

        //then
        assertThat(result.isSuccess()).isTrue();
        verify(observedAnnouncementRepository, times(1)).delete(any(ObservedAnnouncement.class));
    }
}
