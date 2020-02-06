package com.app.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.app.entity.Manufacturer;
import com.app.entity.VehicleModel;
import com.app.enums.VehicleType;
import com.app.repository.ManufacturerRepository;
import com.app.repository.VehicleModelRepository;
import com.app.service.ManufacturerService;
import com.app.utils.Result;

@ExtendWith(SpringExtension.class)
@SpringBootTest
//@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
public class ManufacturerServiceTest {

	@Autowired
	private ManufacturerService manufacturerService;
	
	@Autowired
	private ManufacturerRepository manufacturerRepository;
	
	@Autowired
	private VehicleModelRepository vehicleModelRepository;
	
	@Test
	public void shouldSaveManufacturer() {
		Manufacturer manufacturer = new Manufacturer("mSTmanufacturer1");
		
		Result result = manufacturerService.saveManufacturer(manufacturer);
		
		assertTrue(result.isSuccess());
		assertNotNull(manufacturer.getId());
	}
	
	@Test
	public void shouldSaveManufacturerWithVehicleModels() {
		Manufacturer manufacturer = new Manufacturer("mSTmanufacturer2");
		manufacturer.setVehicleModel(Lists.list(new VehicleModel("mSTvehicleModel1", manufacturer, VehicleType.CAR),
				new VehicleModel("mSTvehicleModel2", manufacturer, VehicleType.CAR)));

		Result result = manufacturerService.saveManufacturer(manufacturer);

		assertTrue(result.isSuccess());
		assertNotNull(manufacturer.getId());
		assertEquals(0, manufacturer.getVehicleModel().stream().filter(e -> e.getId() == null).count());

	}
	
	@Test
	public void shouldReturnValidationErrorBecElementWithSameNameExists() {
		Manufacturer manufacturer = new Manufacturer("mSTmanufacturer3");
		Manufacturer manufacturer2 = new Manufacturer("mSTmanufacturer3");
		
		Result result = manufacturerService.saveManufacturer(manufacturer);
		Result result2 = manufacturerService.saveManufacturer(manufacturer2);
		
		assertTrue(result.isSuccess());
		assertNotNull(manufacturer.getId());
		
		assertTrue(result2.isError());
		assertNull(manufacturer2.getId());
	}
	
	@Test
	public void shouldReturnValidationErrorBecVehicleModelWithSameNameExists() {
		Manufacturer manufacturer = new Manufacturer("mSTmanufacturer4");
		manufacturer.setVehicleModel(Lists.list( new VehicleModel("mSTvehicleModel3", manufacturer, VehicleType.CAR), new VehicleModel("mSTvehicleModel4", manufacturer, VehicleType.CAR)));
		
		Manufacturer manufacturer2 = new Manufacturer("mSTmanufacturer5");
		manufacturer2.setVehicleModel(Lists.list(new VehicleModel("mSTvehicleModel4", manufacturer2, VehicleType.CAR)));
		
		Result result = manufacturerService.saveManufacturer(manufacturer);
		Result result2 = manufacturerService.saveManufacturer(manufacturer2);
		
		assertTrue(result.isSuccess());
		assertNotNull(manufacturer.getId());
		assertEquals(0, manufacturer.getVehicleModel().stream().filter(e -> e.getId() == null).count());
		
		assertTrue(result2.isError());
		assertThat(result2.getValidationResult().get("vehicleModelNameAlreadyExists").contains("vehicleModel4"));
		assertNull(manufacturer2.getId());
		assertEquals(1, manufacturer2.getVehicleModel().stream().filter(e -> e.getId() == null).count());
	}
	
	@Test
	public void shouldDeleteManufacturer() { 
		Manufacturer manufacturer = new Manufacturer("mSTmanufacturer6");
		
		Result saveResult = manufacturerService.saveManufacturer(manufacturer);
		manufacturerService.deleteManufacturer(manufacturer);
		
		assertTrue(saveResult.isSuccess());
		assertNotNull(manufacturer.getId());
		assertFalse(manufacturerRepository.findById(manufacturer.getId()).isPresent());
	}
	
	@Test
	public void shouldDeleteManufacturerWithVehicleModels() { 
		Manufacturer manufacturer = new Manufacturer("manufacturer7");
		manufacturer.setVehicleModel(Lists.list(new VehicleModel("mSTvehicleModel6", manufacturer, VehicleType.CAR), new VehicleModel("vehicleModel7", manufacturer, VehicleType.CAR)));
		
		Result saveResult = manufacturerService.saveManufacturer(manufacturer);
		manufacturerService.deleteManufacturer(manufacturer);
		
		assertTrue(saveResult.isSuccess());
		assertNotNull(manufacturer.getId());
		assertEquals(0, manufacturer.getVehicleModel().stream().filter(e -> e.getId() == null).count());
		assertFalse(manufacturerRepository.findById(manufacturer.getId()).isPresent());
		assertFalse(vehicleModelRepository.findById(manufacturer.getVehicleModel().get(0).getId()).isPresent());
		assertFalse(vehicleModelRepository.findById(manufacturer.getVehicleModel().get(1).getId()).isPresent());
	}
	
}
