package com.app.utils;

import com.app.entity.QAnnouncement;
import com.app.enums.BooleanValuesForDropDown;
import com.app.enums.CarColor;
import com.app.enums.FuelType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanPath;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class AnnouncementSearchFields implements UrlParamsPreparer {
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
    private List<CarColor> carColors = new ArrayList<>();
    private List<FuelType> fuelTypes = new ArrayList<>();
    private String colorListLabelsAsString;

    private BooleanBuilder predicate;
    private StringBuilder urlParams;

    public Predicate prepareQueryAndSearchArguments() {
        predicate = new BooleanBuilder();

        preparePredicate(accidents, QAnnouncement.announcement.accidents);
        preparePredicate(firstOwner, QAnnouncement.announcement.firstOwner);
        preparePredicate(damaged, QAnnouncement.announcement.damaged);
        preparePredicate(netPrice, QAnnouncement.announcement.netPrice);
        preparePredicate(priceNegotiate, QAnnouncement.announcement.priceNegotiate);

        preparePredicateForYear();
        preparePredicateForMileage();
        preparePredicateForEngineCapacity();
        preparePredicateForEnginePower();
        preparePredicateForColor();
        preparePredicateForPrice();
        preparePredicateForDoors();
        preparePredicateForFuelType();
        preparePredicateForCarColor();

        return predicate;
    }

    @Override
    public String prepareUrlParams() {
        urlParams = new StringBuilder();

        addUrlParam("searchFields.accidents", accidents);
        addUrlParam("searchFields.firstOwner", firstOwner);
        addUrlParam("searchFields.damaged", damaged);
        addUrlParam("searchFields.netPrice", netPrice);
        addUrlParam("searchFields.priceNegotiate", priceNegotiate);

        addUrlParam("searchFields.productionYearFrom", productionYearFrom);
        addUrlParam("searchFields.productionYearTo", productionYearTo);

        addUrlParam("searchFields.priceFrom", priceFrom);
        addUrlParam("searchFields.priceTo", priceTo);

        addUrlParam("searchFields.mileageFrom", mileageFrom);
        addUrlParam("searchFields.mileageTo", mileageTo);

        addUrlParam("searchFields.engineCapacityFrom", engineCapacityFrom);
        addUrlParam("searchFields.engineCapacityTo", engineCapacityTo);

        addUrlParam("searchFields.enginePowerFrom", enginePowerFrom);
        addUrlParam("searchFields.enginePowerTo", enginePowerTo);

        urlParams.append("searchFields.colorList=").append(carColors.stream().map(Object::toString).collect(Collectors.joining(","))).append("&");
        urlParams.append("searchFields.fuelTypeList=").append(fuelTypes.stream().map(Object::toString).collect(Collectors.joining(","))).append("&");

        return urlParams.toString();
    }

    private void preparePredicateForDoors() {
        if (doorsFrom != null) {
            predicate.and(QAnnouncement.announcement.doors.goe(doorsFrom));
        }
        if (doorsTo != null) {
            predicate.and(QAnnouncement.announcement.doors.lt(doorsTo));
        }
    }

    private void preparePredicateForPrice() {
        if (priceFrom != null) {
            predicate.and(QAnnouncement.announcement.price.goe(priceFrom));
        }
        if (priceTo != null) {
            predicate.and(QAnnouncement.announcement.price.loe(priceTo));
        }
    }

    private void preparePredicateForColor() {
        if (carColors.size() > 0) {
            predicate.and(QAnnouncement.announcement.carColor.in(carColors));
        }
    }

    private void preparePredicateForEnginePower() {
        if (enginePowerFrom != null) {
            predicate.and(QAnnouncement.announcement.enginePower.goe(enginePowerFrom));
        }
        if (enginePowerTo != null) {
            predicate.and(QAnnouncement.announcement.enginePower.lt(enginePowerTo));
        }
    }

    private void preparePredicateForEngineCapacity() {
        if (engineCapacityFrom != null) {
            predicate.and(QAnnouncement.announcement.engineCapacity.goe(engineCapacityFrom));
        }
        if (engineCapacityTo != null) {
            predicate.and(QAnnouncement.announcement.engineCapacity.lt(engineCapacityTo));
        }
    }

    private void preparePredicateForMileage() {
        if (mileageFrom != null) {
            predicate.and(QAnnouncement.announcement.mileage.goe(mileageFrom));
        }
        if (mileageTo != null) {
            predicate.and(QAnnouncement.announcement.mileage.lt(mileageTo));
        }
    }

    private void preparePredicateForYear() {
        if (productionYearFrom != null) {
            predicate.and(QAnnouncement.announcement.productionYear.goe(productionYearFrom));
        }

        if (productionYearTo != null) {
            predicate.and(QAnnouncement.announcement.productionYear.lt(productionYearTo));
        }
    }

    private void preparePredicate(BooleanValuesForDropDown booleanValuesForDropDown, BooleanPath booleanPath) {
        if (booleanValuesForDropDown != BooleanValuesForDropDown.ALL) {
            predicate.and(booleanPath.eq(booleanValuesForDropDown.getQueryValue()));
        }
    }

    private void preparePredicateForFuelType() {
        if (fuelTypes.size() > 0) {
            predicate.and(QAnnouncement.announcement.fuelType.in(fuelTypes));
        }
    }

    private void preparePredicateForCarColor() {
        if (carColors.size() > 0) {
            predicate.and(QAnnouncement.announcement.carColor.in(carColors));
        }
    }

    public String getSelectedCarColors() {
        return carColors.stream().map(e -> e.getLabel()).collect(Collectors.joining(","));
    }

    public String getSelectedFuelTypes() {
        return fuelTypes.stream().map(e -> e.getLabel()).collect(Collectors.joining(","));
    }

}
