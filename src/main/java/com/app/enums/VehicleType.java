package com.app.enums;

import java.util.LinkedHashMap;
import java.util.Map;

public enum VehicleType {
	CAR("Osobowy"),
	MOTORCYCLE("Dostawczy/ciężarowy"),
	TRUCK( "Motocykl");
	
	private String label;
	
	private VehicleType(String label) {
		this.label = label;
	}
	
	public static Map<VehicleType,String>vehicleTypesWithLabels() {
		Map<VehicleType,String> mapToReturn = new LinkedHashMap<VehicleType,String>();
		mapToReturn.put(VehicleType.CAR, "Osobowy");
		mapToReturn.put(VehicleType.TRUCK, "Dostawczy/ciężarowy");
		mapToReturn.put(VehicleType.MOTORCYCLE, "Motocykl");
		
		return mapToReturn;
	}
	
	public String getLabel() {
		return label;
	}

}
