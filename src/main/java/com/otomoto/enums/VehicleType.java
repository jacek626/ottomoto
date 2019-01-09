package com.otomoto.enums;

import java.util.LinkedHashMap;
import java.util.Map;

public enum VehicleType {
	CAR,
	MOTORCYCLE,
	TRUCK;
	
	public static Map<VehicleType,String>vehicleTypesWithLabels() {
		Map<VehicleType,String> mapToReturn = new LinkedHashMap<VehicleType,String>();
		mapToReturn.put(VehicleType.CAR, "Osobowy");
		mapToReturn.put(VehicleType.TRUCK, "Dostawczy/ciężarowy");
		mapToReturn.put(VehicleType.MOTORCYCLE, "Motocykl");
		
		return mapToReturn;
	}

}
