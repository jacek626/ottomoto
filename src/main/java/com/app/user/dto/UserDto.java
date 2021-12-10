package com.app.user.dto;

import com.app.announcement.entity.Announcement;
import com.app.announcement.entity.ObservedAnnouncement;
import com.app.common.types.Province;
import com.app.user.validator.groups.ValidateAllFieldsWithoutPass;
import com.app.user.validator.groups.ValidatePassOnly;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Transient;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;


@Setter
@Getter
public class UserDto {
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

}

