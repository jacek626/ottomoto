package com.otomoto.enums;

import java.util.LinkedHashMap;
import java.util.Map;

public enum VehicleSubtype {
	MINI,
	CITY_CAR,
	COMPACT, 
	SEDAN, 
	COMBI, 
	MINIVAN, 
	SUV, 
	CABRIO,
	COUPE,
	VANS,
	TRUCK,
	SPORT_BIKE,
	TOURIST_MOTORCYCLE,
	SCOOTER;
	
	public static Map<VehicleSubtype,String>vehicleSubtypesWithLabels(VehicleType vehicleType) {
		Map<VehicleSubtype,String> mapToReturn = new LinkedHashMap<VehicleSubtype,String>();
		if(vehicleType == VehicleType.CAR) {
			mapToReturn.put(VehicleSubtype.MINI, "Auta małe");
			mapToReturn.put(VehicleSubtype.CITY_CAR, "Auta miejskie");
			mapToReturn.put(VehicleSubtype.COMPACT, "Kompakt");
			mapToReturn.put(VehicleSubtype.SEDAN, "Sedan");
			mapToReturn.put(VehicleSubtype.COMBI, "Kombi");
			mapToReturn.put(VehicleSubtype.MINIVAN, "Minivan");
			mapToReturn.put(VehicleSubtype.SUV, "SUV");
			mapToReturn.put(VehicleSubtype.CABRIO, "Kabriolet");
			mapToReturn.put(VehicleSubtype.COUPE, "Coupe");
		}
		else if(vehicleType == VehicleType.TRUCK) {
			mapToReturn.put(VehicleSubtype.VANS, "Auto dostawcze");
			mapToReturn.put(VehicleSubtype.TRUCK, "Auto ciężarowe");
		}
		else if(vehicleType == VehicleType.MOTORCYCLE) {
			mapToReturn.put(VehicleSubtype.SPORT_BIKE, "Sporotwy");
			mapToReturn.put(VehicleSubtype.TOURIST_MOTORCYCLE, "Turystyczny");
			mapToReturn.put(VehicleSubtype.SCOOTER, "Skuter");
		}
		
		return mapToReturn;
	}
		
		

}
