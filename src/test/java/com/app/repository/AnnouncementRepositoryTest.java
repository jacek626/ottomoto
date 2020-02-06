package com.app.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

import org.assertj.core.util.Arrays;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.app.entity.Announcement;
import com.app.entity.Manufacturer;
import com.app.entity.Picture;
import com.app.entity.QAnnouncement;
import com.app.entity.QPicture;
import com.app.entity.User;
import com.app.entity.VehicleModel;
import com.app.enums.VehicleSubtype;
import com.app.enums.VehicleType;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AnnouncementRepositoryTest {
	
	@Autowired
	private AnnouncementRepository announcementRepository;
	
	@Autowired
	private ManufacturerRepository manufacturerRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	private static boolean initialized = false;
	
	private static Announcement announcement;
	
	@BeforeEach
	public void init() {
		if(initialized == false) {
			User user = new User.UserBuilder("announcementRepositoryTestLogin","testPass","testPass","announcementRepositoryMail@test.com", true).build();
			userRepository.save(user);
			Manufacturer manufacturer = new Manufacturer("announcementRepositoryTestManufacturer");
			manufacturer.getVehicleModel().add(new VehicleModel("vehicleModel3" , manufacturer , VehicleType.CAR));
			announcement  = new Announcement.AnnouncementBuilder(manufacturer.getVehicleModel().get(0), VehicleSubtype.COMPACT, 2_000, 180_000, new BigDecimal(100_000), user).setTitle("announcement title").build();
			
			Picture picture1 = new Picture();
			picture1.setAnnouncement(announcement);
			picture1.setRepositoryName("picture1");
			picture1.setMiniatureRepositoryName("picture1");
			picture1.setMainPhotoInAnnouncement(false);
			
			Picture picture2= new Picture();
			picture2.setAnnouncement(announcement);
			picture2.setRepositoryName("picture2");
			picture2.setMiniatureRepositoryName("picture2");
			picture2.setMainPhotoInAnnouncement(true);
			
			
			announcement.setPictures(Lists.list(picture1, picture2));
			
			manufacturerRepository.save(manufacturer);
			announcementRepository.save(announcement);
			initialized = true;
		}
	}
	
	@Test
	public void findFirst10ByDeactivationDateIsNullOrderByCreationDateDescTest() {
		
		List<Announcement> announcementList = announcementRepository.findFirst10ByDeactivationDateIsNullOrderByCreationDateDesc();
		
		assertThat(announcementList).isNotEmpty();
		assertThat(announcementList.size() == 10);
	}
	
	
	@Test
	public void findFirst5ByUserIdAndDeactivationDateIsNullOrderByCreationDateDescTest() {
		
		List<Announcement> announcementList = announcementRepository.findFirst5ByUserIdAndDeactivationDateIsNullOrderByCreationDateDesc(-1L);
		
		assertThat(announcementList).isNotEmpty();
	}
	
	@Test
	public void findFirst5ByUserIdAndFetchPicturesEagerlyTest() {
		List<Announcement> announcementList = announcementRepository.findFirst5ByUserIdAndOtherThenAnnouncementIdFetchPicturesEagerly(-1L, -1L);
		
	  assertThat(announcementList).isNotEmpty();
	}
	
	
	@Test
	public void shouldNotFindAnnouncementByPredictates() {
		List<Announcement> announcementList = announcementRepository.findByPredicates(QAnnouncement.announcement.title.eq("45445"));
		
		assertThat(announcementList).isEmpty();
	}
	
	@Test
	public void shouldFindAnnouncementByPredictates() {
		List<Announcement> announcementList = announcementRepository.findByPredicates(QAnnouncement.announcement.title.eq("announcement title"));
		
		assertThat(announcementList).isNotEmpty();
	}
	
	@Test
	public void shouldFindAnnouncementByPredictatesAndLoadPicture() {
		PageRequest pageable =  PageRequest.of(1, 10, Direction.fromString("ASC"), "id");
		// announcement.vehicleModel.vehicleType = CAR
//		List<Announcement> announcementList = announcementRepository.findByPredicatesAndLoadPictures(pageable, QAnnouncement.announcement.vehicleModel.vehicleType.eq(VehicleType.CAR));
//		Page<Announcement> announcementList = announcementRepository.findByPredicatesAndLoadPicturesForPagination(pageable, QAnnouncement.announcement.vehicleModel.vehicleType.eq(VehicleType.CAR));
//		List<Announcement> announcementList = announcementRepository.findByPredicatesAndLoadPictures(pageable, QAnnouncement.announcement.title.eq("announcement title"), QPicture.picture.mainPhotoInAnnouncement.eq(true));
		Page<Announcement> announcementList = announcementRepository.findByPredicatesAndLoadPicturesForPagination(pageable, QAnnouncement.announcement.title.eq("announcement title"), QPicture.picture.mainPhotoInAnnouncement.eq(true));
		
//		assertThat(announcementList).isNotEmpty();
	//	assertTrue(announcementList.get(0).getPictures().size() == 1);
	//	assertTrue(announcementList.get(0).getPictures().get(0).getRepositoryName().equals("picture2"));
	}
	

}
