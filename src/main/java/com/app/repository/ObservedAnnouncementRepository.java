package com.app.repository;

import com.app.entity.ObservedAnnouncement;
import com.app.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ObservedAnnouncementRepository extends CrudRepository<ObservedAnnouncement, Long>, QuerydslPredicateExecutor<ObservedAnnouncement> {
    List<ObservedAnnouncement> findByUser(User user);

    @Transactional
    @Modifying
    @Query("delete from ObservedAnnouncement where user.id = :userId and announcement.id = :announcementId")
    void deleteByUserIdAndAnnouncementId(long userId, long announcementId);

    @Query("from ObservedAnnouncement oa join oa.user u where u.login = :login")
    List<ObservedAnnouncement> findByUserLogin(String login);

    @Query("select case when count(oa)> 0 then true else false end from ObservedAnnouncement oa join oa.user u where u.login = :login and oa.announcement.id = :announcementId")
    boolean existsByUserLoginAndAnnouncement(String login, long announcementId);
}
