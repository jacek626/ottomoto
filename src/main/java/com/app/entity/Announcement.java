package com.app.entity;

import com.app.enums.*;
import com.app.utils.AnnouncementSearchFields;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.NumberPath;
import lombok.*;
import org.springframework.data.util.Pair;

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
public class Announcement {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq_Announcement")
	@SequenceGenerator(name="seq_Announcement",sequenceName="seq_Announcement")
	private Long id;
	
	@NotBlank
	@Size(max=200)
	@Builder.Default
	private String title = "Default title";
	
	@Size(max=3000)
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

	@Size(max=20)
	private String vin;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Builder.Default
	private FuelType fuelType = FuelType.PETROL;

	@NotNull
	@Min(0)
	@Digits(integer=9, fraction=2)
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

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval=true, mappedBy = "announcement", fetch = FetchType.LAZY)
	@OrderBy("mainPhotoInAnnouncement DESC,id DESC")
	@Singular
	private List<Picture> pictures = new ArrayList<>();

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn
	private User user;

	@Column(updatable = false)
	private Date creationDate;

	private Date deactivationDate;

	@Min(0)
	@Max(value = 50_000, message="{validation.rangeError}")
	private Integer engineCapacity;

	@Max(value=50000, message="{validation.rangeError}")
	private Integer enginePower;
	private Boolean accidents;
	private Boolean firstOwner;
	private Boolean damaged;
	private Byte doors;

	@Transient
	private Long manufacturerId;
	@Transient
	private String manufacturerName;
	@Transient
	private String miniatureRepositoryName;
	@Transient
	private List<Picture> imagesToDelete;
	@Transient
	private VehicleType vehicleType;
	@Transient
	private AnnouncementSearchFields searchFields;
	@Transient
	private List<Predicate> predicates;
	@Transient
	private StringBuilder urlParams;

	public Announcement() {
		searchFields = new AnnouncementSearchFields();
	}

	public Pair<List<Predicate>, String> preparePredicatesAndUrlParams() {
		predicates = new ArrayList<>();
		urlParams = new StringBuilder();

		preparePredicateAndUrlParam(user != null, QAnnouncement.announcement.user.id, (user != null ? user.getId() : null), "user=");
		preparePredicateAndUrlParam(manufacturerId != null, QAnnouncement.announcement.vehicleModel.manufacturer.id, manufacturerId, "manufacturerId=");
		preparePredicateAndUrlParam(vehicleModel != null, QAnnouncement.announcement.vehicleModel.id, (vehicleModel != null ? vehicleModel.getId() : null) , "vehicleModel=");
		preparePredicateAndUrlParamForVehicleType();
		preparePredicateAndUrlParamForVehicleSubtype();

		return Pair.of(predicates, urlParams.toString());
	}

	private void preparePredicateAndUrlParamForVehicleSubtype() {
		if(vehicleSubtype != null) {
			predicates.add(QAnnouncement.announcement.vehicleSubtype.eq(vehicleSubtype));
			urlParams.append("vehicleSubtype=").append(vehicleSubtype).append("&");
		}
	}

	private void preparePredicateAndUrlParamForVehicleType() {
		if(vehicleType != null) {
			predicates.add(QAnnouncement.announcement.vehicleModel.vehicleType.eq(vehicleType));
			urlParams.append("vehicleType=").append(vehicleType).append("&");
		}
	}

	private void preparePredicateAndUrlParam(boolean objectIsNotNull, NumberPath<Long> predicate, Long fieldValue, String urlParam) {
		if (objectIsNotNull) {
			predicates.add(predicate.eq(fieldValue));
			urlParams.append(urlParam).append(fieldValue).append("&");
		}
	}

	public void prepareFieldsForSearch() {
		if(title == null)
			title = "";

		if(vehicleType == null)
			setVehicleType(VehicleType.CAR);
	}

	public AnnouncementSearchFields getSearchFields() {
		if(searchFields == null)
			searchFields = new AnnouncementSearchFields();

		return searchFields;
	}


}
