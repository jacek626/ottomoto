package com.app.announcement.types;

public enum CarColor {
	RED("Red"),
	BROWN("Brown"),
	WHITE("White"),
	BLACK("Black"),
	GREY("Grey"),
	GREEN("Green"),
	YELLOW("Yellow"),
	SILVER("Silver"),
	GOLD("Gold"),
	BLUE("Blue");
	
	private String label;
	
	CarColor(String label) {
		this.setLabel(label);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
