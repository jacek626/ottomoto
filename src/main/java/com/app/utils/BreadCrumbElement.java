package com.app.utils;

public class BreadCrumbElement {
	private String label;
	private String link;
	
	public BreadCrumbElement(String label, String link) {
		super();
		this.label = label;
		this.link = link;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
}
