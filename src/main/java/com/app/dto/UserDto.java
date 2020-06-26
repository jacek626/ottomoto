package com.app.dto;

import com.app.entity.Announcement;
import com.app.entity.ObservedAnnouncement;
import com.app.entity.QUser;
import com.app.enums.Province;
import com.app.searchform.EntityForSearchStrategy;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.*;

import javax.persistence.Transient;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;


@Builder
@Setter
@Getter
@AllArgsConstructor
public class UserDto implements EntityForSearchStrategy {
    private Long id;

    @NotNull
    @NotBlank(message = "{validation.login.notBlank}", groups = {ValidateAllFieldsWithoutPass.class})
    @Size(min = 2, max = 20, groups = {ValidateAllFieldsWithoutPass.class})
    private String login;

    @NotNull
    @NotBlank(message = "{validation.password.notEmpty}", groups = {ValidatePassOnly.class})
    @Size(min = 6, max = 20, message = "{validation.password.size}", groups = {ValidatePassOnly.class})
    private String password;

    @NotNull
    @NotBlank(message = "{validation.mail.notEmpty}", groups = {ValidateAllFieldsWithoutPass.class})
    @Email(groups = {ValidateAllFieldsWithoutPass.class})
    private String email;

    @NotNull
    private Boolean active = Boolean.TRUE;

    @Transient
    private String passwordConfirm;

    @Size(max = 100)
    private String city;

    @Size(max = 1000)
    private String description;

    @Size(max = 100)
    private String firstName;

    @Size(max = 100)
    private String lastName;

    @Max(999999999)
    private Integer phoneNumber;

    @Size(max = 15)
    private String zipCode;

    @Size(max = 150)
    private String street;

    private Date creationDate;

    private Province province;

    private List<Announcement> announcementList;

    private List<ObservedAnnouncement> observedAnnouncementList;

    @Setter(AccessLevel.NONE)
    private BooleanBuilder predicates;
    @Setter(AccessLevel.NONE)
    private StringBuilder urlParams;

    public UserDto() {
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

}

