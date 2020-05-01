package com.app.repository;

import com.app.entity.Manufacturer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ManufacturerRepositoryTest {

	@Autowired
	private ManufacturerRepository manufacturerRepository;

	@Test
	public void findAll() {
		List<Manufacturer> manufacturerList = manufacturerRepository.findAll();

		assertThat(manufacturerList).isNotEmpty();
	}
	
	@Test
	public void saveManufacturer() {
		Manufacturer manufacturer = new Manufacturer();
		manufacturer.setName("LexusManufacturer");
		
		manufacturerRepository.save(manufacturer);
		
		assertThat(manufacturerRepository.findByName(manufacturer.getName())).isNotEmpty();
	}

}
