package com.otomoto.repository;

import java.util.Optional;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.otomoto.entity.Announcement;

@Repository
public interface AnnouncementRepository extends CrudRepository<Announcement, Long>, QuerydslPredicateExecutor<Announcement> {
	Optional<Announcement> findById(Long id);
}
