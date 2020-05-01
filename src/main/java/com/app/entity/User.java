package com.app.entity;

import com.app.enums.Province;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="user_os")
@Getter
@Setter
public class User {
    
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq_user_os")
	@SequenceGenerator(name="seq_user_os",sequenceName="seq_user_os")
    private Long id;
    
	@NotNull
	@NotBlank(message = "{validation.login.notBlank}", groups = {ValidateAllFieldsWithoutPass.class})
    @Size(min=2, max=20, groups = {ValidateAllFieldsWithoutPass.class})
    private String login;
    
	@NotNull
    @NotBlank(message = "{validation.password.notEmpty}", groups = {ValidatePassOnly.class})  
    @Size(min=6, max=20, message = "{validation.password.size}", groups = {ValidatePassOnly.class})
    private String password;
    
	@NotNull
    @NotBlank(message = "{validation.mail.notEmpty}",groups = {ValidateAllFieldsWithoutPass.class})
    @Email(groups = {ValidateAllFieldsWithoutPass.class})
    private String email;
    
    @NotNull
    private Boolean active;
    
    @Transient
    private String passwordConfirm;
    
    @Size(max=100)
	private String city;
	
    @Size(max=1000)
	private String description;
	
	@Size(max=100)
	private String firstName;
	
	@Size(max=100)
	private String lastName;
	
	@Max(999999999)
	private Integer phoneNumber;
	
	@Size(max=15)
	private String zipCode;
	
	@Size(max=150)
	private String street;
	
	@Column(updatable = false) 
	private Date creationDate;
	
	@Enumerated(EnumType.STRING)
	private Province province;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy="user")
	private List<Announcement> announcementList;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<ObservedAnnouncement> observedAnnouncementList;
	
	@OneToOne
	@JoinTable(name = "userRole", joinColumns = @JoinColumn(name = "user_id", referencedColumnName="id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName="id"))
	private Role role;
	
	public static class UserBuilder {
	    private String login;
	    private String password;
	    private String passwordConfirm;
	    private String email;
	    private Boolean active;
		private String city;
		private String description;
		private String firstName;
		private String lastName;
		private Integer phoneNumber;
		private String zipCode;
		private String street;
		private Date creationDate;
		private Province province;
		private List<Announcement> announcementList;
		private Role role;
		
		public UserBuilder(String login, String password, String passwordConfirm, String email, boolean active) {
			this.login = login;
			this.password = password;
			this.passwordConfirm = passwordConfirm;
			this.email = email;
			this.active = active;
		}
		
		public UserBuilder setCity(String city) {
			this.city = city;
			return this;
		}
		
		public UserBuilder setDescription(String description) {
			this.description = description;
			return this;
		}
		
		public UserBuilder setFirstName(String firstName) {
			this.firstName = firstName;
			return this;
		}
		
		public UserBuilder setLastName(String lastName) {
			this.lastName = lastName;
			return this;
		}
		
		public UserBuilder setPhoneNumber(Integer phoneNumber) {
			this.phoneNumber = phoneNumber;
			return this;
		}
		
		public UserBuilder setZipCode(String zipCode) {
			this.zipCode = zipCode;
			return this;
		}
		
		public UserBuilder setCreationDate(Date creationDate) {
			this.creationDate = creationDate;
			return this;
		}
		
		public UserBuilder setProvince(Province province) {
			this.province = province;
			return this;
		}
		
		public UserBuilder setAnnouncementList(List<Announcement> announcementList) {
			this.announcementList = announcementList;
			return this;
		}
		public UserBuilder setRole(Role role) {
			this.role = role;
			return this;
		}
		
		public User build() {
			return new User(this);
		}
		
	}
	
	public User() {
	}
	
	private User(UserBuilder builder) {
		this.login = builder.login;
		this.password = builder.password;
		this.passwordConfirm = builder.passwordConfirm;
		this.email = builder.email;
		this.active = builder.active;
		this.city = builder.city;
		this.description = builder.description;
		this.firstName = builder.firstName;
		this.lastName = builder.lastName;
		this.phoneNumber = builder.phoneNumber;
		this.zipCode = builder.zipCode;
		this.street = builder.street;
		this.creationDate = builder.creationDate;
		this.province = builder.province;
		this.announcementList = builder.announcementList;
		this.role = builder.role;
	}
	
	
	public interface ValidateAllFieldsWithoutPass {
	}
	
	public interface ValidatePassOnly {
	}
	
	public void prepareFiledsForSearch() {
		if(login == null)
			login = "";
		if(email == null)
			email = "";
		if(firstName == null)
			firstName = "";
		if(lastName == null)
			lastName = "";
		if(active == null)
			active = true;
	}


}

