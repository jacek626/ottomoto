package com.otomoto.entity;

public enum VehicleType {
	CARS(0),
	MOTORCYCLES(1),
	TRUCKS(2);
	
	private int id;
	
	VehicleType(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

}
