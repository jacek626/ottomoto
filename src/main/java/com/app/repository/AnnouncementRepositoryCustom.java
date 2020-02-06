package com.app.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.app.entity.Announcement;
import com.querydsl.core.types.Predicate;

public interface AnnouncementRepositoryCustom {
	
	List<Announcement> findByPredicates(Predicate... predicates);
	List<Announcement> findByPredicatesAndLoadMainPicture(Predicate... predicates);
	List<Announcement> findByPredicatesAndLoadPictures(PageRequest pageable, Predicate... predicates);
	Page<Announcement> findByPredicatesAndLoadPicturesForPagination(PageRequest pageable, Predicate... predicates);
}
