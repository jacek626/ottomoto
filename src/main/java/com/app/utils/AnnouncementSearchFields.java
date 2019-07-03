package com.app.utils;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

public class AnnouncementSearchFields {

	private Short productionYearFrom, productionYearTo;
	private String priceFrom, priceTo;
	private String mileageFrom, mileageTo;
	private String engineCapacityFrom, engineCapacityTo; 
	private String enginePowerFrom, enginePowerTo;
	private Byte doorsFrom, doorsTo;
	
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
		return priceFrom.replaceAll("\\s+","");
	}
	public Integer getPriceFromAsInteger() {
		return StringUtils.isNotBlank(priceFrom) ? Integer.valueOf(priceFrom.replaceAll("\\s+","")) : 0;
	}
	public void setPriceFrom(String priceFrom) {
		this.priceFrom = priceFrom;
	}
	public String getPriceTo() {
		return priceTo;
	}
	public String getPriceToWithoutSpaces() {
		return priceTo.replaceAll("\\s+","");
	}
	public Integer getPriceToAsInteger() {
		return StringUtils.isNotBlank(priceTo) ? Integer.valueOf(priceTo.replaceAll("\\s+","")) : 0;
	}
	public void setPriceTo(String priceTo) {
		this.priceTo = priceTo;
	}
	public String getMileageFrom() {
		return mileageFrom;
	}
	public Integer getMileageFromAsInteger() {
		return StringUtils.isNotBlank(mileageFrom) ? Integer.valueOf(mileageFrom.replaceAll("\\s+","")) : 0;
	}
	public void setMileageFrom(String mileageFrom) {
		this.mileageFrom = mileageFrom;
	}
	public String getMileageTo() {
		return mileageTo;
	}
	public Integer getMileageToAsInteger() {
		return StringUtils.isNotBlank(mileageTo) ? Integer.valueOf(mileageTo.replaceAll("\\s+","")) : 0;
	}
	public void setMileageTo(String mileageTo) {
		this.mileageTo = mileageTo;
	}
	public String getEngineCapacityFrom() {
		return engineCapacityFrom;
	}
	public Integer getEngineCapacityFromAsInteger() {
		return StringUtils.isNotBlank(engineCapacityFrom) ? Integer.valueOf(engineCapacityFrom.replaceAll("\\s+","")) : 0;
	}
	public void setEngineCapacityFrom(String engineCapacityFrom) {
		this.engineCapacityFrom = engineCapacityFrom;
	}
	public String getEngineCapacityTo() {
		return engineCapacityTo;
	}
	public Integer getEngineCapacityToAsInteger() {
		return StringUtils.isNotBlank(engineCapacityTo) ? Integer.valueOf(engineCapacityTo.replaceAll("\\s+","")) : 0;
	}
	public void setEngineCapacityTo(String engineCapacityTo) {
		this.engineCapacityTo = engineCapacityTo;
	}
	public String getEnginePowerFrom() {
		return enginePowerFrom;
	}
	public Integer getEnginePowerFromAsInteger() {
		return StringUtils.isNotBlank(enginePowerFrom) ? Integer.valueOf(enginePowerFrom.replaceAll("\\s+","")) : 0;
	}
	public void setEnginePowerFrom(String enginePowerFrom) {
		this.enginePowerFrom = enginePowerFrom;
	}
	public String getEnginePowerTo() {
		return enginePowerTo;
	}
	public Integer getEnginePowerToAsInteger() {
		return StringUtils.isNotBlank(enginePowerTo) ? Integer.valueOf(enginePowerTo.replaceAll("\\s+","")) : 0;
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
	
}
