package com.otomoto.enums;

import java.util.LinkedHashMap;
import java.util.Map;

public enum CarColor {
	RED("Czerwony"),
	BROWN("Brazowy"),
	BLACK("Czarny"),
	GREY("Szary"),
	GREEN("Zielony"),
	YELLOW("Zolty"),
	SILVER("Srebrny"),
	GOLD("Zloty"),
	BLUE("Niebieski");
	
	private String label;
	
	private CarColor(String label) {
		this.setLabel(label);
	}
	
	public static Map<CarColor,String>colorWithLabels() {
		Map<CarColor,String> mapToReturn = new LinkedHashMap<CarColor,String>();
		mapToReturn.put(CarColor.RED, "Czerwony");
		mapToReturn.put(CarColor.BROWN, "Brazowy");
		mapToReturn.put(CarColor.BLACK, "Czarny");
		mapToReturn.put(CarColor.GREY, "Szary");
		mapToReturn.put(CarColor.GREEN, "Zielony");
		mapToReturn.put(CarColor.YELLOW, "Zolty");
		mapToReturn.put(CarColor.SILVER, "Srebrny");
		mapToReturn.put(CarColor.GOLD, "Zloty");
		
		return mapToReturn;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
