package com.app.common.types;

import lombok.Getter;

public enum BooleanValuesForDropDown {
	ALL("All",null),
	YES("Yes", true),
	NO("No", false);

	@Getter
	private final String label;
	@Getter
	private final Boolean queryValue;
	
	BooleanValuesForDropDown(String label, Boolean queryValue) {
		this.label = label;
		this.queryValue = queryValue;
	}


}
