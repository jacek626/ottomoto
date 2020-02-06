package com.app.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.app.enums.BooleanValuesForDropDown;
import com.app.enums.CarColor;
import com.app.enums.FuelType;

public class AnnouncementSearchFields {

	private Short productionYearFrom, productionYearTo;
	private String priceFrom, priceTo;
	private String mileageFrom, mileageTo;
	private String engineCapacityFrom, engineCapacityTo; 
	private String enginePowerFrom, enginePowerTo;
	private Byte doorsFrom, doorsTo;
	private BooleanValuesForDropDown accidents, firstOwner, damaged, netPrice, priceNegotiate;
	private List<CarColor> colorList = new ArrayList<CarColor>();
	private List<FuelType> fuelTypeList = new ArrayList<FuelType>();
	private String colorListLabelsAsString;
	//private List<CarColor> colorList = Arrays.asList(CarColor.BLACK, CarColor.BLUE);
	
	Integer returnAsInteger(String numberAsString) {
		return StringUtils.isNotBlank(numberAsString) ? Integer.valueOf(numberAsString.replaceAll("[^0-9.]","")) : 0;
	}
	
	public Short getProductionYearFrom() {
		return productionYearFrom;
	}
	public Optional<Short> getProductionYearFromAsOpt() {
		return Optional.ofNullable(productionYearFrom);
	}
	public void setProductionYearFrom(Short productionYearFrom) {
		this.productionYearFrom = productionYearFrom;
	}
	public Short getProductionYearTo() {
		return productionYearTo;
	}
	public Optional<Short> getProductionYearToAsOpt() {
		return Optional.ofNullable(productionYearTo);
	}
	public String getPriceFrom() {
		return priceFrom;
	}
	public String getPriceFromWithoutSpaces() {
		return priceFrom.replaceAll("[^0-9.]", "");
	}
	public Integer getPriceFromAsInteger() {
		return returnAsInteger(priceFrom);
	}
	public void setPriceFrom(String priceFrom) {
		this.priceFrom = priceFrom;
	}
	public String getPriceTo() {
		return priceTo;
	}
	public String getPriceToWithoutSpaces() {
		return priceTo.replaceAll("[^0-9.]","");
	}
	public Integer getPriceToAsInteger() {
		return returnAsInteger(priceTo);
	}
	public void setPriceTo(String priceTo) {
		this.priceTo = priceTo;
	}
	public String getMileageFrom() {
		return mileageFrom;
	}
	public Integer getMileageFromAsInteger() {
		return returnAsInteger(mileageFrom);
	}
	public void setMileageFrom(String mileageFrom) {
		this.mileageFrom = mileageFrom;
	}
	public String getMileageTo() {
		return mileageTo;
	}
	public Integer getMileageToAsInteger() {
		return returnAsInteger(mileageTo);
	}
	public void setMileageTo(String mileageTo) {
		this.mileageTo = mileageTo;
	}
	public String getEngineCapacityFrom() {
		return engineCapacityFrom;
	}
	public Integer getEngineCapacityFromAsInteger() {
		return returnAsInteger(engineCapacityFrom);
	}
	public void setEngineCapacityFrom(String engineCapacityFrom) {
		this.engineCapacityFrom = engineCapacityFrom;
	}
	public String getEngineCapacityTo() {
		return engineCapacityTo;
	}
	public Integer getEngineCapacityToAsInteger() {
		return returnAsInteger(engineCapacityTo);
	}
	public void setEngineCapacityTo(String engineCapacityTo) {
		this.engineCapacityTo = engineCapacityTo;
	}
	public String getEnginePowerFrom() {
		return enginePowerFrom;
	}
	public Integer getEnginePowerFromAsInteger() {
		return returnAsInteger(enginePowerFrom);
	}
	public void setEnginePowerFrom(String enginePowerFrom) {
		this.enginePowerFrom = enginePowerFrom;
	}
	public String getEnginePowerTo() {
		return enginePowerTo;
	}
	public Integer getEnginePowerToAsInteger() {
		return returnAsInteger(enginePowerTo);
	}
	public void setEnginePowerTo(String enginePowerTo) {
		this.enginePowerTo = enginePowerTo;
	}
	public void setProductionYearTo(Short productionYearTo) {
		this.productionYearTo = productionYearTo;
	}
	public Byte getDoorsFrom() {
		return doorsFrom;
	}
	public void setDoorsFrom(Byte doorsFrom) {
		this.doorsFrom = doorsFrom;
	}
	public Byte getDoorsTo() {
		return doorsTo;
	}
	public void setDoorsTo(Byte doorsTo) {
		this.doorsTo = doorsTo;
	}
	public BooleanValuesForDropDown getAccidents() {
		if(accidents == null)
			accidents = BooleanValuesForDropDown.ALL;
		
		return accidents;
	}
	public void setAccidents(BooleanValuesForDropDown accidents) {
		this.accidents = accidents;
	}
	public BooleanValuesForDropDown getFirstOwner() {
		if(firstOwner == null)
			firstOwner = BooleanValuesForDropDown.ALL;
		
		return firstOwner;
	}
	public void setFirstOwner(BooleanValuesForDropDown firstOwner) {
		this.firstOwner = firstOwner;
	}
	public BooleanValuesForDropDown getDamaged() {
		if(damaged == null)
			damaged = BooleanValuesForDropDown.ALL;
		
		return damaged;
	}
	public void setDamaged(BooleanValuesForDropDown damaged) {
		this.damaged = damaged;
	}
	public BooleanValuesForDropDown getNetPrice() {
		if(netPrice == null)
			netPrice = BooleanValuesForDropDown.ALL;
		
		return netPrice;
	}
	public void setNetPrice(BooleanValuesForDropDown netPrice) {
		this.netPrice = netPrice;
	}
	public BooleanValuesForDropDown getPriceNegotiate() {
		if(priceNegotiate == null)
			priceNegotiate = BooleanValuesForDropDown.ALL;
		
		return priceNegotiate;
	}
	public void setPriceNegotiate(BooleanValuesForDropDown priceNegotiate) {
		this.priceNegotiate = priceNegotiate;
	}
	public List<CarColor> getColorList() {
		return colorList;
	}
	public String getColorListLabelsAsString() {
		return colorList.stream().map(e -> e.getLabel()).collect(Collectors.joining(","));
	}
	public String getFuelTypeListListLabelsAsString() {
		return fuelTypeList.stream().map(e -> e.getLabel()).collect(Collectors.joining(","));
	}
	public void setColorList(List<CarColor> colorList) {
		this.colorList = colorList;
	}
	public List<FuelType> getFuelTypeList() {
		return fuelTypeList;
	}
	public void setFuelTypeList(List<FuelType> fuelTypeList) {
		this.fuelTypeList = fuelTypeList;
	}
	
}
