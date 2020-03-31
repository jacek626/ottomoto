package com.app.repository;

import com.app.entity.ObservedAnnouncement;
import com.app.entity.User;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObservedAnnouncementRepository extends CrudRepository<ObservedAnnouncement, Long>, QuerydslPredicateExecutor<ObservedAnnouncement> {
    List<ObservedAnnouncement> findByUser(User user);
}
