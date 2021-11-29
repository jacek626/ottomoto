package com.app.manufacturer.entity;

import com.app.searchform.EntityForSearchStrategy;
import com.app.vehiclemodel.entity.VehicleModel;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Entity(name = "Manufacturer")
@Table(name = "manufacturer")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Manufacturer implements EntityForSearchStrategy {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_Manufacturer")
    @SequenceGenerator(name = "seq_Manufacturer", sequenceName = "seq_Manufacturer")
    private Long id;

    @NotBlank
    private String name;

    @OneToMany(mappedBy = "manufacturer", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    @OrderBy(value = "name ASC")
    @Builder.Default
    private List<VehicleModel> vehicleModel = new ArrayList<>();

    @Transient
    private String vehicleModelName;
    @Transient
    private StringBuilder urlParams;
    @Transient
    private BooleanBuilder predicates;

    public Manufacturer(@NotNull String name) {
        super();
        this.name = name;
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

    @Override
    public String prepareUrlParams() {
        urlParams = new StringBuilder();

        addUrlParam("name", name);

        return urlParams.toString();
    }

    @Override
    public Predicate preparePredicates() {
        BooleanBuilder predicate = new BooleanBuilder();

        if (isNotBlank(name)) {
            predicate.and(QManufacturer.manufacturer.name.containsIgnoreCase(name));
        }

        if (isNotBlank(vehicleModelName)) {
            predicate.and(QManufacturer.manufacturer.vehicleModel.any().name.containsIgnoreCase(vehicleModelName));
        }

        return predicate;
    }

    public Optional<List<VehicleModel>> getVehicleModelAsOptional() {
        return Optional.ofNullable(vehicleModel);
    }

    @Override
    public BooleanBuilder getPredicate() {
        return predicates;
    }

}
