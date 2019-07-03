package com.otomoto.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.otomoto.enums.VehicleSubtype;
import com.otomoto.enums.VehicleType;

@Entity
public class VehicleModel {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "seq_VehicleModel")
	@SequenceGenerator(name = "seq_VehicleModel", sequenceName = "seq_VehicleModel")
	private Long id;
	
	@NotNull
	private String name;
	
	@NotNull
	@ManyToOne
	private Manufacturer manufacturer;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private VehicleType vehicleType;
	
/*	@Enumerated(EnumType.STRING)
	private VehicleSubtype vehicleSubtype;*/
	
	@Transient
	private Boolean toDelete = false;

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public VehicleType getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(VehicleType vehicleType) {
		this.vehicleType = vehicleType;
	}

	public Boolean getToDelete() {
		return toDelete;
	}

	public void setToDelete(Boolean toDelete) {
		this.toDelete = toDelete;
	}

/*	public VehicleSubtype getVehicleSubtype() {
		return vehicleSubtype;
	}

	public void setVehicleSubtype(VehicleSubtype vehicleSubtype) {
		this.vehicleSubtype = vehicleSubtype;
	}*/

	public Manufacturer getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(Manufacturer manufacturer) {
		this.manufacturer = manufacturer;
	}

	
}
