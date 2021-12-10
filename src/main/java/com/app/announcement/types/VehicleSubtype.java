package com.app.announcement.types;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum VehicleSubtype {
	CITY_CAR("City car", VehicleType.CAR),
	SMALL("Small", VehicleType.CAR),
	COMPACT("Compact", VehicleType.CAR),
	SEDAN("Sedan", VehicleType.CAR),
	COMBI("Combi", VehicleType.CAR),
	MINIVAN("Minivan", VehicleType.CAR), 
	SUV("SUV", VehicleType.CAR), 
	CABRIO("Cabrio", VehicleType.CAR),
	COUPE("Coupe", VehicleType.CAR),
	VANS("Van", VehicleType.TRUCK),
	TRUCK("Truck", VehicleType.TRUCK),
	SPORT_BIKE("Sport", VehicleType.MOTORCYCLE),
	TOURIST_MOTORCYCLE("Tourist", VehicleType.MOTORCYCLE),
	SCOOTER("Scooter", VehicleType.MOTORCYCLE);
	
	private String label;
	private VehicleType vehicleType;
	
	VehicleSubtype(String label, VehicleType vehicleType) {
		this.setLabel(label);
		this.setVehicleType(vehicleType);
	}
	
	public static List<VehicleSubtype> getVehicleSubtypesByVehicleType(VehicleType vehicleType) {
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
