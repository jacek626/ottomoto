package com.app.common.enums;

public enum VehicleType {
	CAR("Osobowy"),
	MOTORCYCLE("Motocykl"),
	TRUCK( "Dostawczy/ciężarowy");
	
	private final String label;
	
	VehicleType(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}



}
