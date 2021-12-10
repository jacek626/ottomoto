package com.app.announcement.types;

public enum FuelType {
	PETROL("Petrol"),
	DIESEL("Diesel"),
	PETROL_WITH_LPG("Petrol with LPG"),
	HYBRID("Hybrid"),
	ELECTRIC("Electric");
	
	private final String label;
	
	FuelType(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

}
