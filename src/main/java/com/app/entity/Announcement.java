package com.app.entity;

import com.app.enums.*;
import com.app.searchform.EntityForSearchStrategy;
import com.app.utils.search.AnnouncementSearchFields;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
public class Announcement implements EntityForSearchStrategy {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_Announcement")
    @SequenceGenerator(name = "seq_Announcement", sequenceName = "seq_Announcement")
    private Long id;

    @NotBlank
    @Size(max = 200)
    @Builder.Default
    private String title = "Default title";

    @Size(max = 3000)
    private String description;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private VehicleModel vehicleModel;

    @NotNull
    @Min(1900)
    @Max(2100)
    private Integer productionYear;

    @NotNull
    @Min(0)
    @Max(999_999_999)
    @Builder.Default
    private Integer mileage = 0;

    @Size(max = 20)
    private String vin;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private FuelType fuelType = FuelType.PETROL;

    @NotNull
    @Min(0)
    @Digits(integer = 9, fraction = 2)
    private BigDecimal price;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Builder.Default
	private Currency currency = Currency.PLN;

	@NotNull
	@Builder.Default
	private Boolean netPrice = false;

	@NotNull
	@Builder.Default
	private Boolean priceNegotiate = true;

    @NotNull
    @Enumerated(EnumType.STRING)
    private VehicleSubtype vehicleSubtype;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private CarColor carColor = CarColor.WHITE;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "announcement", fetch = FetchType.LAZY)
    @OrderBy("mainPhotoInAnnouncement DESC,id DESC")
    @Singular
    private List<Picture> pictures = new ArrayList<>();

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User user;

    @Column(updatable = false)
    private Date creationDate;

    private Boolean active = true;

    @Min(0)
    @Max(value = 50_000, message = "{validation.rangeError}")
    @NotNull
    private Integer engineCapacity;

    @Max(value = 50000, message = "{validation.rangeError}")
    @NotNull
    private Integer enginePower;

    private Boolean accidents;
    private Boolean firstOwner;
    private Boolean damaged;
    private Byte doors;

    @Transient
    private Long manufacturerId;
    @Transient
    private String miniatureRepositoryName;
	@Transient
	private VehicleType vehicleType;
	@Transient
	private StringBuilder urlParams;
	@Transient
	@Builder.Default
	private AnnouncementSearchFields searchFields = new AnnouncementSearchFields();
	@Transient
	@Getter(AccessLevel.NONE)
	private BooleanBuilder predicates;

	public Announcement() {
	}

	@Override
	public String prepareUrlParams() {
		urlParams = new StringBuilder();

		addUrlParam("user", user);
		addUrlParam("manufacturerId", manufacturerId);
		addUrlParam("vehicleModel.id", vehicleModel);
		addUrlParam("vehicleType", vehicleType);
		addUrlParam("vehicleSubtype", vehicleSubtype);

		urlParams.append(getSearchFields().prepareUrlParams());

		return urlParams.toString();
	}

	@Override
	public Predicate preparePredicates() {
		predicates = new BooleanBuilder();

		preparePredicates(QAnnouncement.announcement.user.id, (user == null ? null : user.getId()));
		preparePredicates(QAnnouncement.announcement.vehicleModel.manufacturer.id, manufacturerId);
		preparePredicates(QAnnouncement.announcement.vehicleModel.id, (vehicleModel == null ? null : vehicleModel.getId()));
		preparePredicateForVehicleType();
		preparePredicateForVehicleSubtype();

		predicates.and(getSearchFields().prepareQueryAndSearchArguments());

		return predicates;
	}

	@Override
	public BooleanBuilder getPredicate() {
		return predicates;
	}

	private void preparePredicateForVehicleSubtype() {
		if (vehicleSubtype != null) {
			predicates.and(QAnnouncement.announcement.vehicleSubtype.eq(vehicleSubtype));
		}
	}

	private void preparePredicateForVehicleType() {
		if (vehicleType != null)
            predicates.and(QAnnouncement.announcement.vehicleModel.vehicleType.eq(vehicleType));
    }

    public AnnouncementSearchFields getSearchFields() {
        if (searchFields == null)
            searchFields = new AnnouncementSearchFields();

        return searchFields;
    }

    public VehicleType getVehicleType() {
        if (vehicleType == null)
            vehicleType = (vehicleModel == null ? VehicleType.CAR : vehicleModel.getVehicleType());

        return vehicleType;
    }

    public Long getManufacturerId() {
        if (vehicleModel != null && vehicleModel.getManufacturer() != null)
            return vehicleModel.getManufacturer().getId();

        return manufacturerId;
    }

}
