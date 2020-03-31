package com.app.enums;

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

/*	public static List<FuelType>fuelTypeWithLabels2() {
		return Stream.of(FuelType.values()).collect(Collectors.toList());
	}*/
	
/*	public static Map<FuelType,String>fuelTypeWithLabels() {
		Map<FuelType,String> mapToReturn = new LinkedHashMap<FuelType,String>();
		mapToReturn.put(FuelType.PETROL, "Benzyna");
		mapToReturn.put(FuelType.DIESEL, "Diesel");
		mapToReturn.put(FuelType.PETROL_WITH_LPG, "Benzyna+LPG");
		mapToReturn.put(FuelType.HYBRID, "Hybryda");
		mapToReturn.put(FuelType.ELECTRIC, "Elektryczny");
		
		return mapToReturn;
	}*/

	public String getLabel() {
		return label;
	}

	//public void setLabel(String label) {
	//	this.label = label;
//	}
}
