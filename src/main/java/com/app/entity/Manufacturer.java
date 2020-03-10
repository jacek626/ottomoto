package com.app.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
	private List<VehicleModel> vehicleModel = new ArrayList<>();
	
	@Transient
	private String vehicleModelName;
	
	public Manufacturer(@NotNull String name) {
		super();
		this.name = name;
	}
	
	public Manufacturer() {
	}

	public Manufacturer(@NotNull String name, List<VehicleModel> vehicleModel) {
		super();
		this.name = name;
		this.vehicleModel = vehicleModel;
	}

	public Manufacturer(Long id, String name) {
		this.id = id;
		this.name = name;
	}

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Manufacturer that = (Manufacturer) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
