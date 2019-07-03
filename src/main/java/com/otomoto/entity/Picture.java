package com.otomoto.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

@Entity
public class Picture {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq_Picture")
	@SequenceGenerator(name="seq_Picture",sequenceName="seq_Picture")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "announcement_id")
	private Announcement announcement;
	
	private String repositoryName;
	private String miniatureRepositoryName;
	
	private String fileName;
	
	@Transient
	private boolean pictureToDelete = false;
	
	public Announcement getAnnouncement() {
		return announcement;
	}

	public void setAnnouncement(Announcement announcement) {
		this.announcement = announcement;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getRepositoryName() {
		return repositoryName;
	}

	public void setRepositoryName(String repositoryName) {
		this.repositoryName = repositoryName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public boolean isPictureToDelete() {
		return pictureToDelete;
	}

	public void setPictureToDelete(boolean pictureToDelete) {
		this.pictureToDelete = pictureToDelete;
	}

	public String getMiniatureRepositoryName() {
		return miniatureRepositoryName;
	}

	public void setMiniatureRepositoryName(String miniatureRepositoryName) {
		this.miniatureRepositoryName = miniatureRepositoryName;
	}

}
