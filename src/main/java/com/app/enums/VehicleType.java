package com.app.enums;

public enum VehicleType {
	CAR("Osobowy"),
	MOTORCYCLE("Dostawczy/ciężarowy"),
	TRUCK( "Motocykl");
	
	private String label;
	
	private VehicleType(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}

}
