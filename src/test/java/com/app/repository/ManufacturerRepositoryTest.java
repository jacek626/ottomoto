package com.app.repository;

import com.app.entity.Manufacturer;
import com.app.entity.VehicleModel;
import com.app.enums.VehicleType;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ManufacturerRepositoryTest {

    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @Autowired
    private VehicleModelRepository vehicleModelRepository;

/*	@Test
	public void findAll() {
		List<Manufacturer> manufacturerList = manufacturerRepository.findAll();

		assertThat(manufacturerList).isNotEmpty();
	}*/

    @Test
    public void saveManufacturer() {
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setName("LexusManufacturer");

        manufacturerRepository.save(manufacturer);

        assertThat(manufacturerRepository.findByName(manufacturer.getName())).isNotEmpty();
    }

    @Test
    public void saveManufacturerAndVehicleModel() {
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setName("LexusManufacturer");
        manufacturer.setVehicleModel(Lists.newArrayList());

        VehicleModel vehicleModel1 = VehicleModel.builder().name("test1").vehicleType(VehicleType.CAR).manufacturer(manufacturer).build();
        VehicleModel vehicleModel2 = VehicleModel.builder().name("test2").vehicleType(VehicleType.CAR).manufacturer(manufacturer).build();
        manufacturer.getVehicleModel().add(vehicleModel1);
        manufacturer.getVehicleModel().add(vehicleModel2);

        manufacturerRepository.save(manufacturer);

        assertThat(manufacturerRepository.findByName(manufacturer.getName())).isNotEmpty();
        assertThat(vehicleModelRepository.findByName(vehicleModel1.getName())).isNotNull();

    }

}
