package com.app.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.app.entity.Announcement;

@Repository("announcementRepository")
public interface AnnouncementRepository extends CrudRepository<Announcement, Long>, QuerydslPredicateExecutor<Announcement>, AnnouncementRepositoryCustom {
	Optional<Announcement> findById(Long id);
	
	boolean existsByIdAndDeactivationDateIsNull(Long id);
	boolean existsByUserIdAndDeactivationDateIsNull(Long userId);
	
	
	
	/*
	 * @Modifying
	 * 
	 * @Query("update Announcement a set deactivationDate = null where a.id = :announcementId"
	 * ) int activateAnnouncement(@Param("announcementId") Long announcementId);
	 */
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("update Announcement a set deactivationDate = :deactivationDate where a.user.id = :userId")
	int updateDeactivationDateByUserId(@Param("deactivationDate") Date deactivationDate, @Param("userId") Long userId);
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("update Announcement a set deactivationDate = :deactivationDate where a.id = :announcementId")
	int updateDeactivationDateByAnnouncementId(@Param("deactivationDate") Date deactivationDate, @Param("announcementId") Long announcementId);
	
	// JOIN FETCH m.vehicleModel
	@Query("SELECT a FROM Announcement a JOIN FETCH a.vehicleModel v  JOIN FETCH v.manufacturer  JOIN FETCH a.pictures p  WHERE p.mainPhotoInAnnouncement = true and a.user.id = :userId and a.id != :announcementId and a.deactivationDate is NULL order by a.creationDate")
	List<Announcement> findFirst5ByUserIdAndOtherThenAnnouncementIdFetchPicturesEagerly(@Param("announcementId") Long announcementId, @Param("userId") Long userId);
	//List<Announcement> findAnnouncementByUserId
	//@Query("SELECT p FROM Person p JOIN FETCH p.roles WHERE p.id = (:id)")
	
	
	List<Announcement> findFirst10ByDeactivationDateIsNullOrderByCreationDateDesc();
	List<Announcement> findFirst20ByDeactivationDateIsNullOrderByCreationDateDesc();
	List<Announcement> findFirst5ByUserIdAndDeactivationDateIsNullOrderByCreationDateDesc(Long userId);
	
	//int coutByVehicleModelManufacturerId(Long manufacturerId);
	
//	public List<Person> find(@Param("lastName") String lastName);
}
