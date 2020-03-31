package com.app.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
public class Role {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="seq_Role")
	@SequenceGenerator(name="seq_Role",sequenceName="seq_Role")
	private Long id;
	
	private String name;

}
