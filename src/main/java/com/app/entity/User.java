package com.app.entity;

import com.app.enums.Province;
import com.app.searchform.EntityForSearchStrategy;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;


@Table(name = "user_os")
@Builder
@Setter
@Getter
@AllArgsConstructor
@Entity
public class User implements EntityForSearchStrategy {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_user_os")
    @SequenceGenerator(name = "seq_user_os", sequenceName = "seq_user_os")
    private Long id;

    @NotNull
    @NotBlank(message = "{validation.login.notBlank}", groups = {ValidateAllFieldsWithoutPass.class})
    @Size(min = 2, max = 20, groups = {ValidateAllFieldsWithoutPass.class})
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
    private Boolean active = Boolean.FALSE;
    
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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    private List<Announcement> announcementList;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ObservedAnnouncement> observedAnnouncementList;

    @OneToOne
    @JoinTable(name = "userRole", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
//	@JoinTable( joinColumns = @JoinColumn(referencedColumnName="id"), inverseJoinColumns = @JoinColumn(referencedColumnName="id"))
    private Role role;

    @Transient
    @Setter(AccessLevel.NONE)
    private BooleanBuilder predicates;
    @Transient
    @Setter(AccessLevel.NONE)
    private StringBuilder urlParams;

    public User() {
    }

    @Override
    public Predicate preparePredicates() {
        predicates = new BooleanBuilder();

        preparePredicates(QUser.user.login, getLogin());
        preparePredicates(QUser.user.email, getEmail());
        preparePredicates(QUser.user.firstName, getFirstName());
        preparePredicates(QUser.user.lastName, getLastName());
        preparePredicates(QUser.user.active, getActive());
        preparePredicates(QUser.user.city, getCity());

        return predicates;
    }

    @Override
    public BooleanBuilder getPredicate() {
        return predicates;
    }

    @Override
    public String prepareUrlParams() {
        urlParams = new StringBuilder();

        addUrlParam("login", login);
        addUrlParam("email", email);
        addUrlParam("firstName", firstName);
        addUrlParam("lastName", lastName);
        addUrlParam("active", active);
        addUrlParam("city", city);

        return urlParams.toString();
    }

    @Override
    public StringBuilder getUrlParams() {
        return urlParams;
    }

    public interface ValidateAllFieldsWithoutPass {
    }

    public interface ValidatePassOnly {
    }

    public void prepareFiledsForSearch() {
        if (login == null)
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

