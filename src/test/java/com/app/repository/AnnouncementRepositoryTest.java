package com.app.repository;

import com.app.entity.Announcement;
import com.app.entity.QAnnouncement;
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

/*	@BeforeEach
	public void init() {
		if(!initialized) {
            User user = User.builder().login("announcementRepositoryTestLogin").password("testPass").passwordConfirm("testPass").email("announcementRepositoryMail@test.com").active(true).build();
            userRepository.save(user);
            Manufacturer manufacturer = Manufacturer.builder().name("announcementRepositoryTestManufacturer").build();
            manufacturer.getVehicleModel().add(VehicleModel.builder().name("vehicleModel3").manufacturer(manufacturer).vehicleType(VehicleType.CAR).build());
            Announcement announcement = Announcement.builder().title("announcement title").user(user).vehicleModel(manufacturer.getVehicleModel().get(0)).vehicleSubtype(VehicleSubtype.COMPACT).productionYear(2_000).price(BigDecimal.valueOf(180_000)).build();

            Picture picture1 = Picture.builder().build();
            picture1.setAnnouncement(announcement);
            picture1.setRepositoryName("picture1");
            picture1.setMiniatureRepositoryName("picture1");
            picture1.setMainPhotoInAnnouncement(false);

            Picture picture2 = Picture.builder().build();
            picture2.setAnnouncement(announcement);
            picture2.setRepositoryName("picture2");
			picture2.setMiniatureRepositoryName("picture2");
			picture2.setMainPhotoInAnnouncement(true);
			
			
			announcement.setPictures(Lists.list(picture1, picture2));
			
			manufacturerRepository.save(manufacturer);
			announcementRepository.save(announcement);
			initialized = true;
		}
	}*/
	
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
	
/*	@Test
	public void shouldFindAnnouncementByPredictates() {
		List<Announcement> announcementList = announcementRepository.findByPredicates(QAnnouncement.announcement.title.eq("announcement title"));
		
		assertThat(announcementList).isNotEmpty();
	}*/
	
	@Test
	public void shouldFindAnnouncementByPredicatesAndLoadPicture() {
        PageRequest pageable = PageRequest.of(1, 10, Sort.Direction.fromString("ASC"), "id");
        Page<Announcement> announcementList = announcementRepository.findByPredicatesAndLoadMainPicture(pageable, QAnnouncement.announcement.active.eq(true));

        assertThat(announcementList).isNotEmpty();
    }

}
