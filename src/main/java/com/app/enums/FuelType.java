package com.app.enums;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum FuelType {
	PETROL("Benzyna"),
	DIESEL("Diesel"),
	PETROL_WITH_LPG("Benzyna+LPG"),
	HYBRID("Hybryda"),
	ELECTRIC("Elektryczny");
	
	private String label;
	
	private FuelType(String label) {
		this.setLabel(label);
	}

	public static List<FuelType>fuelTypeWithLabels2() {
		return Stream.of(FuelType.values()).collect(Collectors.toList());
	}
	
	public static Map<FuelType,String>fuelTypeWithLabels() {
		Map<FuelType,String> mapToReturn = new LinkedHashMap<FuelType,String>();
		mapToReturn.put(FuelType.PETROL, "Benzyna");
		mapToReturn.put(FuelType.DIESEL, "Diesel");
		mapToReturn.put(FuelType.PETROL_WITH_LPG, "Benzyna+LPG");
		mapToReturn.put(FuelType.HYBRID, "Hybryda");
		mapToReturn.put(FuelType.ELECTRIC, "Elektryczny");
		
		return mapToReturn;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
