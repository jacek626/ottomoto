package com.otomoto.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cascade;

@Entity
public class Manufacturer {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@NotNull
	private String name;
	
	@OneToMany(cascade=CascadeType.ALL)
	@OrderBy(value = "name ASC")
	private List<VehicleModel> vehicleModel;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<VehicleModel> getVehicleModel() {
		return vehicleModel;
	}

	public void setVehicleModel(List<VehicleModel> vehicleModel) {
		this.vehicleModel = vehicleModel;
	}

	
}
