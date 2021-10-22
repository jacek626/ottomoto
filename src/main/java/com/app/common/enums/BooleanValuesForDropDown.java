package com.app.common.enums;

import lombok.Getter;

public enum BooleanValuesForDropDown {
	ALL("Wszystkie",null),
	YES("Tak", true),
	NO("Nie", false);

	@Getter
	private final String label;
	@Getter
	private final Boolean queryValue;
	
	BooleanValuesForDropDown(String label, Boolean queryValue) {
		this.label = label;
		this.queryValue = queryValue;
	}


}
