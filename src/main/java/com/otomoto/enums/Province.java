package com.otomoto.enums;

import java.util.LinkedHashMap;
import java.util.Map;

public enum Province {
	LUBELSKIE,
	PODKARPACKIE,
	MAZOWIECKIE;

	public static Map<Province,String>provinceWithLabels() {
		Map<Province,String> mapToReturn = new LinkedHashMap<Province,String>();
		mapToReturn.put(Province.LUBELSKIE, "Lubelskie");
		mapToReturn.put(Province.PODKARPACKIE, "Podkarpackie");
		mapToReturn.put(Province.MAZOWIECKIE, "Mazowieckie");
		
		return mapToReturn;
	}
}
