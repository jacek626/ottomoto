package com.app.repository;

import com.app.entity.Announcement;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface AnnouncementRepositoryCustom {

	List<Announcement> findByPredicates(Predicate... predicates);
	long countByPredicates(Predicate... predicates);
	List<Announcement> findByPredicatesAndLoadMainPicture(Predicate... predicates);
	List<Announcement> findByPredicatesAndLoadPictures(PageRequest pageable, Predicate... predicates);
	Page<Announcement> findByPredicatesAndLoadPicturesForPagination(PageRequest pageable, Predicate... predicates);
}
