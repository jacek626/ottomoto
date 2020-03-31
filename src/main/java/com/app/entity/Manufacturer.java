package com.app.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
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

}
