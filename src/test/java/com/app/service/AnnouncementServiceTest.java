package com.app.service;

import com.app.entity.Announcement;
import com.app.entity.Picture;
import com.app.repository.AnnouncementRepository;
import com.app.utils.TestUtils;
import com.app.utils.validation.Result;
import com.app.validator.AnnouncementValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AnnouncementServiceTest {
	@Mock
	private AnnouncementRepository announcementRepository;

	@InjectMocks
    @SuppressWarnings("unused")
    private final AnnouncementValidator announcementValidator = spy(AnnouncementValidator.class);

	@InjectMocks
	private AnnouncementService announcementService;

	@Test
	public void shouldSaveAnnouncement() {
        //given
        Announcement announcement = TestUtils.prepareAnnouncement();

        //when
        Result result = announcementService.saveAnnouncement(announcement);

        //then
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

        //then
        assertThat(result.isSuccess()).isTrue();
        verify(announcementRepository, times(1)).save(any(Announcement.class));
    }
	
	@Test
	public void shouldDeactivateAnnouncement() {
        //given
        Announcement announcement = TestUtils.prepareAnnouncement();
        announcement.setId(-999L);
        when(announcementRepository.existsByUserIdAndActiveIsTrue(any(Long.class))).thenReturn(true);

        //when
        Result deactivationResult = announcementService.deactivateAnnouncement(announcement.getId());

        //then
        assertThat(deactivationResult.isSuccess()).isTrue();
        verify(announcementRepository, times(1)).deactivateByAnnouncementId(any(Long.class));

	}


}
