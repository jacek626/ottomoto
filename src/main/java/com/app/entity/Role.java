package com.app.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
public class Role {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="seq_Role")
	@SequenceGenerator(name="seq_Role",sequenceName="seq_Role")
	private Long id;
	
	private String name;

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
