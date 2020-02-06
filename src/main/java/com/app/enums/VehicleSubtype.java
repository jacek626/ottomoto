package com.app.enums;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum VehicleSubtype {
	CITY_CAR("Auta miejskie", VehicleType.CAR),
	SMALL("Auta małe", VehicleType.CAR),
	COMPACT("Kompakt", VehicleType.CAR), 
	SEDAN("Sedan", VehicleType.CAR), 
	COMBI("Kombi", VehicleType.CAR), 
	MINIVAN("Minivan", VehicleType.CAR), 
	SUV("SUV", VehicleType.CAR), 
	CABRIO("Kabriolet", VehicleType.CAR),
	COUPE("Coupe", VehicleType.CAR),
	VANS("Auto dostawcze", VehicleType.TRUCK),
	TRUCK("Auto ciężarowe", VehicleType.TRUCK),
	SPORT_BIKE("Sporotwy", VehicleType.MOTORCYCLE),
	TOURIST_MOTORCYCLE("Turystyczny", VehicleType.MOTORCYCLE),
	SCOOTER("Skuter", VehicleType.MOTORCYCLE);
	
	private String label;
	private VehicleType vehicleType;
	
	private VehicleSubtype(String label, VehicleType vehicleType) {
		this.setLabel(label);
		this.setVehicleType(vehicleType);
	}
	
	public static List<VehicleSubtype>vehicleSubtypesWithLabels2(VehicleType vehicleType) {
		return Stream.of(VehicleSubtype.values()).filter(s -> s.getVehicleType() == vehicleType).collect(Collectors.toList());
	}
	
	public static Map<VehicleSubtype,String>vehicleSubtypesWithLabels(VehicleType vehicleType) {
		Map<VehicleSubtype,String> mapToReturn = new LinkedHashMap<VehicleSubtype,String>();
		if(vehicleType == VehicleType.CAR) {
			mapToReturn.put(VehicleSubtype.SMALL, "Auta małe");
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

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public VehicleType getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(VehicleType vehicleType) {
		this.vehicleType = vehicleType;
	}
		
		

}
