package com.app.enums;

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
	
	public static List<VehicleSubtype>vehicleSubtypesWithLabels(VehicleType vehicleType) {
		return Stream.of(VehicleSubtype.values()).filter(s -> s.getVehicleType() == vehicleType).collect(Collectors.toList());
	}

	public static Map<VehicleSubtype,String> vehicleSubtypesWMap(VehicleType vehicleType) {
		return null;
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
