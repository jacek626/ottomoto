package com.app.repository;

import com.app.announcement.entity.Announcement;
import com.app.announcement.entity.QAnnouncement;
import com.app.announcement.repository.AnnouncementRepository;
import com.app.manufacturer.repository.ManufacturerRepository;
import com.app.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class AnnouncementRepositoryTest {

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @Autowired
    private UserRepository userRepository;

    private static final boolean initialized = false;

	@Test
	public void findFirst10ByDeactivationDateIsNullOrderByCreationDateDescTest() {

        List<Announcement> announcementList = announcementRepository.findFirst10ByActiveIsTrueOrderByCreationDateDesc();

        assertThat(announcementList).isNotEmpty();
        assertThat(announcementList.size() == 10);
    }
	
	
	@Test
	public void findFirst5ByUserIdAndDeactivationDateIsNullOrderByCreationDateDescTest() {

        List<Announcement> announcementList = announcementRepository.findFirst5ByUserIdAndActiveIsTrueOrderByCreationDateDesc(-1L);

        assertThat(announcementList).isNotEmpty();
    }
	
	@Test
	public void findFirst5ByUserIdAndFetchPicturesEagerlyTest() {
        List<Announcement> announcementList = announcementRepository.findOtherUserAnnouncements(-1L, -1L);

        assertThat(announcementList).isNotEmpty();
    }
	
	
	@Test
	public void shouldNotFindAnnouncementByPredictates() {
		List<Announcement> announcementList = announcementRepository.findByPredicates(QAnnouncement.announcement.title.eq("45445"));
		
		assertThat(announcementList).isEmpty();
	}
	
	@Test
	public void shouldFindAnnouncementByPredicatesAndLoadPicture() {
        PageRequest pageable = PageRequest.of(1, 10, Sort.Direction.fromString("ASC"), "id");
        Page<Announcement> announcementList = announcementRepository.findByPredicatesAndLoadMainPicture(pageable, QAnnouncement.announcement.active.eq(true));

        assertThat(announcementList).isNotEmpty();
    }

}
