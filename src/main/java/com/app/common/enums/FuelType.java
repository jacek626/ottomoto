package com.app.common.enums;

public enum FuelType {
	PETROL("Benzyna"),
	DIESEL("Diesel"),
	PETROL_WITH_LPG("Benzyna+LPG"),
	HYBRID("Hybryda"),
	ELECTRIC("Elektryczny");
	
	private final String label;
	
	FuelType(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

}
