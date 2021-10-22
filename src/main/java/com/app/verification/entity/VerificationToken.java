package com.app.verification.entity;

import com.app.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_VerificationToken")
    @SequenceGenerator(name = "seq_VerificationToken", sequenceName = "seq_VerificationToken")
    private Long id;

    @NotNull
    private String token;

    @NotNull
    @OneToOne
    @JoinColumn
    private User user;

    public VerificationToken() {
    }

    public VerificationToken(User user) {
        this.user = user;
    }

}
