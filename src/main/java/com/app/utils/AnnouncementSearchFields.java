package com.app.utils;

import com.app.entity.QAnnouncement;
import com.app.enums.BooleanValuesForDropDown;
import com.app.enums.CarColor;
import com.app.enums.FuelType;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanPath;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class AnnouncementSearchFields {
	private Integer productionYearFrom;
	private Integer productionYearTo;
	private Integer priceFrom;
	private Integer priceTo;
	private Integer mileageFrom;
	private Integer mileageTo;
	private Integer engineCapacityFrom;
	private Integer engineCapacityTo;
	private Integer enginePowerFrom;
	private Integer enginePowerTo;
	private Byte doorsFrom;
	private Byte doorsTo;
	private BooleanValuesForDropDown accidents = BooleanValuesForDropDown.ALL;
	private BooleanValuesForDropDown firstOwner = BooleanValuesForDropDown.ALL;
	private BooleanValuesForDropDown damaged = BooleanValuesForDropDown.ALL;
	private BooleanValuesForDropDown netPrice = BooleanValuesForDropDown.ALL;
	private BooleanValuesForDropDown priceNegotiate = BooleanValuesForDropDown.ALL;
	private List<CarColor> colorList = new ArrayList<>();
	private List<FuelType> fuelTypeList = new ArrayList<>();
	private String colorListLabelsAsString;

	private List<Predicate> predicates;
	private StringBuilder urlParams;

	public Pair<List<Predicate>, String> prepareQueryAndSearchArguments() {
		predicates = new ArrayList<>();
		urlParams = new StringBuilder();

		preparePredicateAndUrlParam(accidents, QAnnouncement.announcement.accidents, "searchFields.accidents=");
		preparePredicateAndUrlParam(firstOwner, QAnnouncement.announcement.firstOwner, "searchFields.firstOwner=");
		preparePredicateAndUrlParam(damaged, QAnnouncement.announcement.damaged, "searchFields.damaged=");
		preparePredicateAndUrlParam(netPrice, QAnnouncement.announcement.netPrice, "searchFields.netPrice=");
		preparePredicateAndUrlParam(priceNegotiate, QAnnouncement.announcement.priceNegotiate, "searchFields.priceNegotiate=");

		preparePredicateAndUrlParamForYear();
		preparePredicateAndUrlParamForMileage();
		preparePredicateAndUrlParamForEngineCapacity();
		preparePredicateAndUrlParamForEnginePower();
		preparePredicateAndUrlParamForColor();
		preparePredicateAndUrlParamForPrice();
		preparePredicateAndUrlParamForDoors();

		return Pair.of(predicates, urlParams.toString());
	}

	private void preparePredicateAndUrlParamForDoors() {
		if(doorsFrom != null) {
			predicates.add(QAnnouncement.announcement.doors.goe(doorsFrom));
			urlParams.append("productionYearFrom=").append(doorsFrom).append("&");
		}
		if(doorsTo != null) {
			predicates.add(QAnnouncement.announcement.doors.lt(doorsTo));
			urlParams.append("productionYearTo=").append(doorsTo).append("&");
		}
	}

	private void preparePredicateAndUrlParamForPrice() {
		if(priceFrom != null) {
			predicates.add(QAnnouncement.announcement.price.goe(priceFrom));
			urlParams.append("searchFields.priceFrom=").append(priceFrom).append("&");
		}
		if(priceTo != null) {
			predicates.add(QAnnouncement.announcement.price.loe(priceTo));
			urlParams.append("searchFields.priceTo=").append(priceTo).append("&");
		}
	}

	private void preparePredicateAndUrlParamForColor() {
		if(colorList.size() > 0) {
			predicates.add(QAnnouncement.announcement.carColor.in(colorList));
			urlParams.append("searchFields.colorList=").append(colorList.stream().map(Object::toString).collect(Collectors.joining())).append("&");
		}
	}

	private void preparePredicateAndUrlParamForEnginePower() {
		if(enginePowerFrom != null) {
			predicates.add(QAnnouncement.announcement.enginePower.goe(enginePowerFrom));
			urlParams.append("searchFields.enginePowerFrom=").append(enginePowerFrom).append("&");
		}
		if(enginePowerTo != null) {
			predicates.add(QAnnouncement.announcement.enginePower.lt(enginePowerTo));
			urlParams.append("searchFields.enginePowerTo=").append(enginePowerTo).append("&");
		}
	}

	private void preparePredicateAndUrlParamForEngineCapacity() {
		if(engineCapacityFrom != null) {
			predicates.add(QAnnouncement.announcement.engineCapacity.goe(engineCapacityFrom));
			urlParams.append("searchFields.productionYearFrom=").append(engineCapacityFrom).append("&");
		}
		if(engineCapacityTo != null) {
			predicates.add(QAnnouncement.announcement.engineCapacity.lt(engineCapacityTo));
			urlParams.append("searchFields.productionYearTo=").append(engineCapacityTo).append("&");
		}
	}

	private void preparePredicateAndUrlParamForMileage() {
		if(mileageFrom != null) {
			predicates.add(QAnnouncement.announcement.mileage.goe(mileageFrom));
			urlParams.append("searchFields.productionYearFrom=").append(mileageFrom).append("&");
		}
		if(mileageTo != null) {
			predicates.add(QAnnouncement.announcement.mileage.lt(mileageTo));
			urlParams.append("searchFields.productionYearTo=").append(mileageTo).append("&");
		}
	}

	private void preparePredicateAndUrlParamForYear() {
		if(productionYearFrom != null) {
			predicates.add(QAnnouncement.announcement.productionYear.goe(productionYearFrom));
			urlParams.append("searchFields.productionYearFrom=").append(productionYearFrom).append("&");
		}

		if(productionYearTo != null) {
			predicates.add(QAnnouncement.announcement.productionYear.lt(productionYearTo));
			urlParams.append("searchFields.productionYearTo=").append(productionYearTo).append("&");
		}
	}

	private void preparePredicateAndUrlParam(BooleanValuesForDropDown accidents, BooleanPath accidents2, String s) {
		if (accidents != BooleanValuesForDropDown.ALL) {
			predicates.add(accidents2.eq(accidents.getQueryValue()));
			urlParams.append(s).append(accidents == BooleanValuesForDropDown.YES).append("&");
		}
	}

	public String getSelectedCarColors() {
		return colorList.stream().map(e -> e.getLabel()).collect(Collectors.joining(","));
	}
	public String getSelectedFuelTypes() {
		return fuelTypeList.stream().map(e -> e.getLabel()).collect(Collectors.joining(","));
	}


}
