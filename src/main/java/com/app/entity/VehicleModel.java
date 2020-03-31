package com.app.entity;

import com.app.enums.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Setter
@Getter
@AllArgsConstructor
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

	public VehicleModel(@NotNull Long id,@NotNull String name, @NotNull Manufacturer manufacturer, @NotNull VehicleType vehicleType) {
		super();
		this.id = id;
		this.name = name;
		this.manufacturer = manufacturer;
		this.vehicleType = vehicleType;
	}

	public VehicleModel(@NotNull String name, @NotNull Manufacturer manufacturer, @NotNull VehicleType vehicleType) {
		super();
		this.name = name;
		this.manufacturer = manufacturer;
		this.vehicleType = vehicleType;
	}
	
	public VehicleModel() {
	}

}