package com.app.repository;

import com.app.entity.ObservedAnnouncement;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObservedAnnouncementRepository extends CrudRepository<ObservedAnnouncement, Long>, QuerydslPredicateExecutor<ObservedAnnouncement> {


}
