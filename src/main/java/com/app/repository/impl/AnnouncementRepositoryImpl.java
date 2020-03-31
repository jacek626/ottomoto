package com.app.repository.impl;

import com.app.entity.Announcement;
import com.app.entity.Picture;
import com.app.entity.QAnnouncement;
import com.app.entity.QPicture;
import com.app.repository.AnnouncementRepositoryCustom;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class AnnouncementRepositoryImpl implements AnnouncementRepositoryCustom {
	
	@PersistenceContext
	private EntityManager entityManager;

	public List<Announcement> findByPredicates(Predicate... predicates) {
		QAnnouncement announcement = QAnnouncement.announcement;
		JPAQuery<Announcement> query = new JPAQuery<>(entityManager);
	    
		return query.from(announcement).where(predicates).fetch();
	}

	@Override
	public long countByPredicates(Predicate... predicates) {
		QAnnouncement announcement = QAnnouncement.announcement;
		JPAQuery<Announcement> query = new JPAQuery<>(entityManager);


		return query.from(announcement).where(predicates).fetchCount();
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
	
	private OrderSpecifier[] getOrderSpecifiers(@NotNull Pageable pageable, @NotNull Class klass) {
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

	public Page<Announcement> findByPredicatesAndLoadMainPicture(PageRequest pageable, Collection<Predicate> predicates, EntityPath<?> ... from) {
		JPAQuery<Announcement> announcementQuery = new JPAQuery<>(entityManager);
		JPAQuery<Picture> pictureQuery = new JPAQuery<>(entityManager);

		List<Announcement> announcements =
				announcementQuery.
				from(QAnnouncement.announcement).
				from(from).
				where(predicates.stream().toArray(Predicate[]::new)).
				offset((pageable.getPageNumber()) * pageable.getPageSize()).
				limit(pageable.getPageSize()).
				orderBy(getOrderSpecifiers(pageable, Announcement.class)).
				fetch();

		Map<Long, Announcement> announcementsAsMap = announcements.stream().collect(Collectors.toMap(Announcement::getId, a -> a));

		List<Tuple> pictures =
				pictureQuery.select(QPicture.picture.miniatureRepositoryName, QPicture.picture.announcement.id).from(QPicture.picture).
				where(QPicture.picture.mainPhotoInAnnouncement.eq(true).and(QPicture.picture.announcement.id.in(announcements.stream().map(Announcement::getId).collect(Collectors.toList())))).
				fetch();

		for(Tuple picture : pictures) {
			announcementsAsMap.get(picture.get(QPicture.picture.announcement.id)).setMiniatureRepositoryName(picture.get(QPicture.picture.miniatureRepositoryName));
		}

		return new PageImpl<>(announcements, pageable,announcementQuery.fetchCount());
	}

}