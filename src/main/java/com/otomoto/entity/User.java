package com.otomoto.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.logging.log4j.util.Strings;

import com.otomoto.enums.Province;

import antlr.StringUtils;

@Entity
@Table(name="user_os")
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
    
    @NotNull(groups = {ValidateAllFieldsWithoutPass.class})
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
	
	@Enumerated(EnumType.STRING)
	private Province province;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy="user")
	private List<Announcement> announcementList;
	
	@OneToOne
	@JoinTable(name = "userRole", joinColumns = @JoinColumn(name = "user_id", referencedColumnName="id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName="id"))
	private Role role;
	
	
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public List<Announcement> getAnnouncementList() {
		return announcementList;
	}

	public void setAnnouncementList(List<Announcement> announcementList) {
		this.announcementList = announcementList;
	}


	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
    
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Integer getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(Integer phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}
}

