package com.otomoto.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity
public class Manufacturer {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="seq_Manufacturer")
	@SequenceGenerator(name="seq_Manufacturer", sequenceName="seq_Manufacturer")
	private Long id;
	
	@NotNull
	private String name;
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@OrderBy(value = "name ASC")
	private List<VehicleModel> vehicleModel = new ArrayList<VehicleModel>();
	
	@Transient
	private String vehicleModelName;
	
	public void prepareFiledsForSearch() {
		if (name == null)
			name = "";

		if (vehicleModelName == null)
			vehicleModelName = "";
	}

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

	public List<VehicleModel> getVehicleModel() {
		return vehicleModel;
	}

	public void setVehicleModel(List<VehicleModel> vehicleModel) {
		this.vehicleModel = vehicleModel;
	}

	public String getVehicleModelName() {
		return vehicleModelName;
	}

	public void setVehicleModelName(String vehicleModelName) {
		this.vehicleModelName = vehicleModelName;
	}
	
}
