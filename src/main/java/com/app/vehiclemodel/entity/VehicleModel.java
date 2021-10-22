package com.app.vehiclemodel.entity;

import com.app.common.enums.VehicleType;
import com.app.common.utils.search.ObjectWithId;
import com.app.manufacturer.entity.Manufacturer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Setter
@Getter
@AllArgsConstructor
@Builder
public class VehicleModel implements ObjectWithId {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_VehicleModel")
    @SequenceGenerator(name = "seq_VehicleModel", sequenceName = "seq_VehicleModel")
    private Long id;

    @NotEmpty
    private String name;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Manufacturer manufacturer;

    @NotNull
    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;
	
	@Transient
	private Boolean toDelete = false;

	public VehicleModel() {
	}

}
