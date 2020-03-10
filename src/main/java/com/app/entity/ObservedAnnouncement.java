package com.app.entity;

import javax.persistence.*;

@Entity
public class ObservedAnnouncement {


    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="seq_ObservedAnnouncement")
    @SequenceGenerator(name="seq_ObservedAnnouncement",sequenceName="seq_ObservedAnnouncement")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "announcement_id")
    private Announcement announcement;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ObservedAnnouncement(Announcement announcement, User user) {
        this.announcement = announcement;
        this.user = user;
    }

    public Announcement getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(Announcement announcement) {
        this.announcement = announcement;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
