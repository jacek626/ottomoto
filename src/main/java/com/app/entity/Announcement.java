package com.app.entity;

import com.app.enums.*;
import com.app.utils.AnnouncementSearchFields;
import com.google.common.collect.Lists;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Announcement {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq_Announcement")
	@SequenceGenerator(name="seq_Announcement",sequenceName="seq_Announcement")
	private Long id;
	
	@NotBlank
	@Size(max=200)
	private String title;
	
	@Size(max=3000)
	private String description;

	@Transient
	private Long manufacturerId;

	@Transient
	private String manufacturerName;

	@Transient
	private VehicleType vehicleType;

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
	//@Size(min=0, max=999_999_999, message="{validation.rangeError}")
	private Integer mileage;

	@Size(max=20)
	private String vin;

	@NotNull
	@Enumerated(EnumType.STRING)
	private FuelType fuelType;

	@NotNull
	@Min(0)
	@Digits(integer=9, fraction=2)
	private BigDecimal price;

	@NotNull
	@Enumerated(EnumType.STRING)
	private Currency currency;

	@NotNull
	private Boolean netPrice;

	@NotNull
	private Boolean priceNegotiate;

	@NotNull
	@Enumerated(EnumType.STRING)
	private VehicleSubtype vehicleSubtype;

	@NotNull
	@Enumerated(EnumType.STRING)
	private CarColor carColor;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval=true, mappedBy = "announcement", fetch = FetchType.EAGER)
	@OrderBy("mainPhotoInAnnouncement DESC,id DESC")
	private List<Picture> pictures = new ArrayList<Picture>();

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn
	private User user;

	@Column(updatable = false)
	private Date creationDate;

	private Date deactivationDate;

	//@Size(min=0,max=999_999, message="{validation.rangeError}")

	@Min(0)
	@Max(value = 50_000, message="{validation.rangeError}")
	private Integer engineCapacity;
	//@Size(min=0,max=999_999, message="{validation.rangeError}")

	@Max(value=50000, message="{validation.rangeError}")
	private Integer enginePower;
	private Boolean accidents;

	private Boolean firstOwner;

	private Boolean damaged;

	private Byte doors;

	@Transient
	private AnnouncementSearchFields searchFields = new AnnouncementSearchFields();

	public static final Announcement newInstanceForAnnouncementCreationForm() {
		return new Announcement.AnnouncementBuilder().build();
	}
	public static class AnnouncementBuilder {

		private String title;
		private String description;
		private Manufacturer manufacturer;
		private VehicleModel vehicleModel;
		private Integer productionYear;
		private Integer mileage;
		private String vin;
		private FuelType fuelType;
		private BigDecimal price;
		private Currency currency;
		private Boolean netPrice;
		private Boolean priceNegotiate;
		private VehicleType vehicleType;
		private VehicleSubtype vehicleSubtype;
		private CarColor carColor;
		private List<Picture> pictures = new ArrayList<Picture>();
		private User user;
		private Date creationDate;
		private Date deactivationDate;
		private Integer engineCapacity;
		private Integer enginePower;
		private Boolean accidents;
		private Boolean firstOwner;
		private Boolean damaged;
		private Byte doors;

		public AnnouncementBuilder(VehicleModel vehicleModel, VehicleSubtype vehicleSubtype, Integer productionYear, Integer mileage, BigDecimal price, User user) {
			this.vehicleModel = vehicleModel;
			this.vehicleSubtype = vehicleSubtype;
			this.productionYear = productionYear;
			this.mileage = mileage;
			this.price = price;
			this.user = user;

			setDefaultValues();
		}

		private AnnouncementBuilder() {
			setDefaultValues();
		}

		private void setDefaultValues() {
		    this.title = "Default title";
			this.carColor = CarColor.WHITE;
			this.currency = Currency.PLN;
			this.netPrice = false;
			this.priceNegotiate = true;
			this.fuelType = FuelType.PETROL;
			this.pictures = Lists.newArrayList();
		}
		
		public Announcement build() {
			return new Announcement(this);
		}
		
		
		public AnnouncementBuilder setTitle(String title) {
			this.title = title;
			return this;
		}

		public AnnouncementBuilder setDescription(String description) {
			this.description = description;
			return this;
		}

		public AnnouncementBuilder setManufacturer(Manufacturer manufacturer) {
			this.manufacturer = manufacturer;
			return this;
		}

		public AnnouncementBuilder setVehicleModel(VehicleModel vehicleModel) {
			this.vehicleModel = vehicleModel;
			return this;
		}

		public AnnouncementBuilder setProductionYear(Integer productionYear) {
			this.productionYear = productionYear;
			return this;
		}

		public AnnouncementBuilder setMileage(Integer mileage) {
			this.mileage = mileage;
			return this;
		}

		public AnnouncementBuilder setVin(String vin) {
			this.vin = vin;
			return this;
		}

		public AnnouncementBuilder setFuelType(FuelType fuelType) {
			this.fuelType = fuelType;
			return this;
		}

		public AnnouncementBuilder setPrice(BigDecimal price) {
			this.price = price;
			return this;
		}

		public AnnouncementBuilder setCurrency(Currency currency) {
			this.currency = currency;
			return this;
		}

		public AnnouncementBuilder setNetPrice(Boolean netPrice) {
			this.netPrice = netPrice;
			return this;
		}

		public AnnouncementBuilder setPriceNegotiate(Boolean priceNegotiate) {
			this.priceNegotiate = priceNegotiate;
			return this;
		}

		public AnnouncementBuilder setVehicleType(VehicleType vehicleType) {
			this.vehicleType = vehicleType;
			return this;
		}

		public AnnouncementBuilder setVehicleSubtype(VehicleSubtype vehicleSubtype) {
			this.vehicleSubtype = vehicleSubtype;
			return this;
		}

		public AnnouncementBuilder setCarColor(CarColor carColor) {
			this.carColor = carColor;
			return this;
		}

		public AnnouncementBuilder setPictures(List<Picture> pictures) {
			this.pictures = pictures;
			return this;
		}

		public AnnouncementBuilder setUser(User user) {
			this.user = user;
			return this;
		}

		public AnnouncementBuilder setCreationDate(Date creationDate) {
			this.creationDate = creationDate;
			return this;
		}

		public AnnouncementBuilder setDeactivationDate(Date deactivationDate) {
			this.deactivationDate = deactivationDate;
			return this;
		}

		public AnnouncementBuilder setEngineCapacity(Integer engineCapacity) {
			this.engineCapacity = engineCapacity;
			return this;
		}

		public AnnouncementBuilder setEnginePower(Integer enginePower) {
			this.enginePower = enginePower;
			return this;
		}

		public AnnouncementBuilder setAccidents(Boolean accidents) {
			this.accidents = accidents;
			return this;
		}

		public AnnouncementBuilder setFirstOwner(Boolean firstOwner) {
			this.firstOwner = firstOwner;
			return this;
		}

		public AnnouncementBuilder setDamaged(Boolean damaged) {
			this.damaged = damaged;
			return this;
		}

		public AnnouncementBuilder setDoors(Byte doors) {
			this.doors = doors;
			return this;
		}
	}
	
	public void prepareFiledsForSearch() {
		if(title == null)
			title = "";

		if(vehicleType == null)
			setVehicleType(VehicleType.CAR);
	}
	
	public Announcement() {
	}
	
	private Announcement(AnnouncementBuilder announcementBuilder) {
		this.vehicleModel = announcementBuilder.vehicleModel;
		this.vehicleSubtype = announcementBuilder.vehicleSubtype;
		this.productionYear = announcementBuilder.productionYear;
		this.mileage = announcementBuilder.mileage;
		this.price = announcementBuilder.price;
		this.user = announcementBuilder.user;
		this.title = announcementBuilder.title;
		this.carColor = announcementBuilder.carColor;
		this.currency = announcementBuilder.currency;
		this.netPrice = announcementBuilder.netPrice;
		this.priceNegotiate = announcementBuilder.priceNegotiate;
		this.fuelType = announcementBuilder.fuelType;
		this.pictures = announcementBuilder.pictures;
	}

	public Long getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public VehicleModel getVehicleModel() {
		return vehicleModel;
	}

	public void setVehicleModel(VehicleModel vehicleModel) {
		this.vehicleModel = vehicleModel;
	}

	public Integer getProductionYear() {
		return productionYear;
	}

	public void setProductionYear(Integer productionYear) {
		this.productionYear = productionYear;
	}

	public Integer getMileage() {
		return mileage;
	}

	public void setMileage(Integer mileage) {
		this.mileage = mileage;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public FuelType getFuelType() {
		return fuelType;
	}

	public void setFuelType(FuelType fuelType) {
		this.fuelType = fuelType;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public Boolean getNetPrice() {
		return netPrice;
	}

	public void setNetPrice(Boolean netPrice) {
		this.netPrice = netPrice;
	}

	public Boolean getPriceNegotiate() {
		return priceNegotiate;
	}

	public void setPriceNegotiate(Boolean priceNegotiate) {
		this.priceNegotiate = priceNegotiate;
	}

	public CarColor getCarColor() {
		return carColor;
	}

	public void setCarColor(CarColor carColor) {
		this.carColor = carColor;
	}

	public List<Picture> getPictures() {
		return pictures;
	}

	public void setPictures(List<Picture> pictures) {
		this.pictures = pictures;
	}

	public VehicleType getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(VehicleType vehicleType) {
		this.vehicleType = vehicleType;
	}

	public VehicleSubtype getVehicleSubtype() {
		return vehicleSubtype;
	}

	public void setVehicleSubtype(VehicleSubtype vehicleSubtype) {
		this.vehicleSubtype = vehicleSubtype;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getDeactivationDate() {
		return deactivationDate;
	}

	public void setDeactivationDate(Date deactivationDate) {
		this.deactivationDate = deactivationDate;
	}

	public Boolean getAccidents() {
		return accidents;
	}

	public void setAccidents(Boolean accidents) {
		this.accidents = accidents;
	}

	public Boolean getFirstOwner() {
		return firstOwner;
	}

	public void setFirstOwner(Boolean firstOwner) {
		this.firstOwner = firstOwner;
	}

	public Boolean getDamaged() {
		return damaged;
	}

	public void setDamaged(Boolean damaged) {
		this.damaged = damaged;
	}

	public Integer getEngineCapacity() {
		return engineCapacity;
	}

	public void setEngineCapacity(Integer engineCapacity) {
		this.engineCapacity = engineCapacity;
	}

	public Integer getEnginePower() {
		return enginePower;
	}

	public void setEnginePower(Integer enginePower) {
		this.enginePower = enginePower;
	}

	public AnnouncementSearchFields getSearchFields() {
		return searchFields;
	}

	public void setSearchFields(AnnouncementSearchFields searchFields) {
		this.searchFields = searchFields;
	}

	public Byte getDoors() {
		return doors;
	}

	public void setDoors(Byte doors) {
		this.doors = doors;
	}

	public Long getManufacturerId() {
		return manufacturerId;
	}

	public void setManufacturerId(Long manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	public String getManufacturerName() {
		return manufacturerName;
	}

	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}
}
