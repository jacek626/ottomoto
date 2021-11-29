package com.app.vehiclemodel.entity;

import com.app.common.enums.VehicleType;
import com.app.common.utils.search.ObjectWithId;
import com.app.manufacturer.entity.Manufacturer;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"toDelete"})
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
}
