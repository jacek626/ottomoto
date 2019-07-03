package com.otomoto.enums;

public enum BooleanValuesForDropDown {
	ALL("Wszystkie"),
	YES("Tak"),
	NO("Nie");
	
	private String label;
	
	private BooleanValuesForDropDown(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

}
