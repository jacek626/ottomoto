package com.app.enums;

public enum CarColor {
	RED("Czerwony"),
	BROWN("Brazowy"),
	WHITE("Bialy"),
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

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
