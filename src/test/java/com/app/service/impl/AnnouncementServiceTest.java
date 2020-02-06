package com.app.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.app.entity.Announcement;
import com.app.entity.Manufacturer;
import com.app.entity.User;
import com.app.entity.VehicleModel;
import com.app.enums.VehicleSubtype;
import com.app.enums.VehicleType;
import com.app.repository.AnnouncementRepository;
import com.app.service.AnnouncementService;
import com.app.service.ManufacturerService;
import com.app.service.UserService;
import com.app.utils.Result;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AnnouncementServiceTest {

	@Autowired
	private AnnouncementService announcementService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ManufacturerService manufacturerService;
	
	@Autowired
	private AnnouncementRepository announcementRepository;
	
	
	@Test
	public void shouldSaveAnnouncement() {
		User user = new User.UserBuilder("userLoginTest1","testPass","testPass","mailTest1@test.com", true).build();
		Manufacturer manufacturer = new Manufacturer("manufacturer1");
		VehicleModel vehicleModel = new VehicleModel("vehicleModel1" , manufacturer ,VehicleType.CAR);
		manufacturer.getVehicleModel().add(vehicleModel);
		Announcement announcement  = new Announcement.AnnouncementBuilder(vehicleModel, VehicleSubtype.COMPACT, 2_000, 180_000, new BigDecimal(20_200), user).build();
		
		Result resultOfUserSave = userService.saveUser(user);
		Result resultOfManufacturerSave = manufacturerService.saveManufacturer(manufacturer);
		Result resultOfAnnouncemenetSave = announcementService.saveAnnouncement(announcement);
		
		assertTrue(resultOfUserSave.isSuccess());
		assertTrue(resultOfManufacturerSave.isSuccess());
		assertTrue(resultOfAnnouncemenetSave.isSuccess());
		assertNotNull(user.getId());
		assertNotNull(announcement.getId());
		assertNotNull(manufacturer.getId());
	}
	
	@Test
	public void shouldReturnValidationErrorBecouseUserIsNotDefined() {
		Manufacturer manufacturer = new Manufacturer("manufacturer2");
		manufacturer.getVehicleModel().add(new VehicleModel("vehicleModel2" , manufacturer , VehicleType.CAR));
		manufacturerService.saveManufacturer(manufacturer);
		Announcement announcement  = new Announcement.AnnouncementBuilder(manufacturer.getVehicleModel().get(0), VehicleSubtype.COMPACT, 2_000, 180_000, new BigDecimal(20_200), null).build();
		
		Result resultOfManufacturerSave = manufacturerService.saveManufacturer(manufacturer);
		Result resultOfAnnouncemenetSave = announcementService.saveAnnouncement(announcement);
		
		assertTrue(resultOfManufacturerSave.isSuccess());
		assertTrue(resultOfAnnouncemenetSave.isError());
		assertEquals(resultOfAnnouncemenetSave.getValidationResult().get("user"), "isEmpty");
	}
	
	@Test
	public void shouldReturnValidationErrorBecousePriceIsBelowZero() {
		User user = new User.UserBuilder("userLoginTest2","testPass","testPass","mailTest2@test.com", true).build();
		Manufacturer manufacturer = new Manufacturer("announcemenetServiceTestManufacturer3");
		manufacturer.getVehicleModel().add(new VehicleModel("announcemenetServiceTestVehicleModel3" , manufacturer , VehicleType.CAR));
		manufacturerService.saveManufacturer(manufacturer);
		Announcement announcement  = new Announcement.AnnouncementBuilder(manufacturer.getVehicleModel().get(0), VehicleSubtype.COMPACT, 2_000, 180_000, new BigDecimal(-1), user).build();
		
		Result resultOfManufacturerSave = manufacturerService.saveManufacturer(manufacturer);
		Result resultOfAnnouncemenetSave = announcementService.saveAnnouncement(announcement);
		
		assertTrue(resultOfManufacturerSave.isSuccess());
		assertTrue(resultOfAnnouncemenetSave.isError());
		assertEquals(resultOfAnnouncemenetSave.getValidationResult().get("price"), "isNegative");
	}
	
	@Test
	public void shouldReturnValidationErrorBecouseUserIsDeactivated() {
		User user = new User.UserBuilder("userLoginTest3","testPass","testPass","mailTest3@test.com", true).build();
		user.setActive(false);
		userService.saveUser(user);
		Manufacturer manufacturer = new Manufacturer("manufacturer4");
		manufacturer.getVehicleModel().add(new VehicleModel("vehicleModel4" , manufacturer , VehicleType.CAR));
		manufacturerService.saveManufacturer(manufacturer);
		Announcement announcement  = new Announcement.AnnouncementBuilder(manufacturer.getVehicleModel().get(0), VehicleSubtype.COMPACT, 2_000, 180_000, new BigDecimal(30_000), user).build();
		
		Result resultOfManufacturerSave = manufacturerService.saveManufacturer(manufacturer);
		Result resultOfAnnouncemenetSave = announcementService.saveAnnouncement(announcement);
		
		assertTrue(resultOfManufacturerSave.isSuccess());
		assertTrue(resultOfAnnouncemenetSave.isError());
		assertEquals(resultOfAnnouncemenetSave.getValidationResult().get("user"), "isDeactivated");
	}
	
	@Test
	public void shouldDeactivateAnnouncemenet() {
		User user = new User.UserBuilder("userLoginTest4","testPass","testPass","mailTest4@test.com", true).build();
		userService.saveUser(user);
		Manufacturer manufacturer = new Manufacturer("manufacturer5");
		manufacturer.getVehicleModel().add(new VehicleModel("vehicleModel5" , manufacturer , VehicleType.CAR));
		manufacturerService.saveManufacturer(manufacturer);
		Announcement announcement  = new Announcement.AnnouncementBuilder(manufacturer.getVehicleModel().get(0), VehicleSubtype.COMPACT, 2_000, 180_000, new BigDecimal(30_000), user).build();
		
		
		Result saveResult = announcementService.saveAnnouncement(announcement);
		Result deactivationResult = announcementService.deactivateAnnouncement(announcement.getId());
		
		assertTrue(saveResult.isSuccess());
		assertTrue(deactivationResult.isSuccess());
		assertNotNull(announcementRepository.findById(announcement.getId()).get().getDeactivationDate());
		
	}
	
}
