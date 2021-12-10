package com.app.announcement.types;

public enum VehicleType {
	CAR("Car"),
	MOTORCYCLE("Motorcycle"),
	TRUCK( "Truck");
	
	private final String label;
	
	VehicleType(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}



}
