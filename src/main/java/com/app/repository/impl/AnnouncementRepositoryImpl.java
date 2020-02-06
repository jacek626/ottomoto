package com.app.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.app.entity.Announcement;
import com.app.entity.QAnnouncement;
import com.app.entity.QPicture;
import com.app.repository.AnnouncementRepositoryCustom;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;

public class AnnouncementRepositoryImpl implements AnnouncementRepositoryCustom {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public List<Announcement> findByPredicates(Predicate... predicates) {
		QAnnouncement announcement = QAnnouncement.announcement;
		JPAQuery<Announcement> query = new JPAQuery<>(entityManager);
	    
		return query.from(announcement).where(predicates).fetch();
	}
	
	public List<Announcement> findByPredicatesAndLoadMainPicture(Predicate... predicates) {
		QAnnouncement announcement = QAnnouncement.announcement;
		QPicture picture = QPicture.picture;
		JPAQuery<Announcement> query = new JPAQuery<>(entityManager);
		
		return 
				query.from(announcement).
				leftJoin(announcement.pictures, picture).
				on(picture.mainPhotoInAnnouncement.eq(true)).
				where(predicates).fetch();
	}
	
	@SuppressWarnings("rawtypes")
	private OrderSpecifier[] getOrderSpecifiers(@NotNull Pageable pageable, @NotNull Class klass) {

	    // orderVariable must match the variable of FROM
	    String className = klass.getSimpleName();
	    final String orderVariable = String.valueOf(Character.toLowerCase(className.charAt(0))).concat(className.substring(1));

	    return pageable.getSort().stream()
	            .map(order -> new OrderSpecifier(
	                    Order.valueOf(order.getDirection().toString()),
	                    new PathBuilder(klass, orderVariable).get(order.getProperty()))
	            )
	            .toArray(OrderSpecifier[]::new);
	}
	
	public List<Announcement> findByPredicatesAndLoadPictures(PageRequest pageable, Predicate... predicates) {
		QAnnouncement announcement = QAnnouncement.announcement;
		QPicture picture = QPicture.picture;
		JPAQuery<Announcement> query = new JPAQuery<>(entityManager);
		
		List<Announcement> result =  
				query.from(announcement).
				leftJoin(announcement.pictures, picture).
				fetchJoin().
				where(predicates).
				offset((pageable.getPageNumber()) * pageable.getPageSize()).
				limit(pageable.getPageSize()).
				orderBy(getOrderSpecifiers(pageable, Announcement.class)).
				fetch();
		
		return result;
	}
	
	public Page<Announcement> findByPredicatesAndLoadPicturesForPagination(PageRequest pageable, Predicate... predicates) {
		QAnnouncement announcement = QAnnouncement.announcement;
		QPicture picture = QPicture.picture;
		JPAQuery<Announcement> query = new JPAQuery<>(entityManager);
		
		List<Announcement> result =  
				query.from(announcement).
				leftJoin(announcement.pictures, picture).
				where(picture.mainPhotoInAnnouncement.eq(true).or(picture.isNull())).
				fetchJoin().
				where(predicates).
				offset((pageable.getPageNumber()) * pageable.getPageSize()).
				limit(pageable.getPageSize()).
				orderBy(getOrderSpecifiers(pageable, Announcement.class)).
				fetch();
		
	 //       int pageSize = pageable.getPageSize();
//		      long pageOffset = pageable.getOffset();
//		       long total = pageOffset + result.size() + (result.size() == pageSize ? pageSize : 0);
		       long total = query.fetchCount();
	       Page<Announcement> page = new PageImpl<Announcement>(result, pageable,total);
		
	       return page;
	}
	

}
