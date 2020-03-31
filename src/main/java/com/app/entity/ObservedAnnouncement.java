package com.app.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
public class ObservedAnnouncement {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_ObservedAnnouncement")
    @SequenceGenerator(name = "seq_ObservedAnnouncement", sequenceName = "seq_ObservedAnnouncement")
    private Long id;

    @ManyToOne
    @JoinColumn
    private Announcement announcement;

    @ManyToOne
    @JoinColumn
    private User user;

    public ObservedAnnouncement() {
    }

    public ObservedAnnouncement(Announcement announcement, User user) {
        this.announcement = announcement;
        this.user = user;
    }

}

