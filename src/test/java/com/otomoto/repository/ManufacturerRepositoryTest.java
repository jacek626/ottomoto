package com.otomoto.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.otomoto.entity.Manufacturer;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ManufacturerRepositoryTest {

	@Autowired
	ManufacturerRepository manufacturerRepository;

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
