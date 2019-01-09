package com.otomoto.entity;

public enum CarType {
	SMALLCAR(0),
	BIGCAR(1),
	MEDIUM(2);
	
	private int id;
	
	CarType(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
