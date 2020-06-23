package com.app.repository;

import com.app.entity.Announcement;
import com.app.entity.VehicleModel;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnnouncementRepository extends CrudRepository<Announcement, Long>, QuerydslPredicateExecutor<Announcement>, AnnouncementRepositoryCustom {

    @Override
    @NonNull
    @Query(value = "SELECT a FROM Announcement a LEFT JOIN FETCH a.vehicleModel vm LEFT JOIN FETCH vm.manufacturer LEFT JOIN FETCH a.user LEFT JOIN FETCH a.pictures  where a.id = :id")
    Optional<Announcement> findById(@NonNull Long id);

    //boolean existsByIdAndDeactivationDateIsNull(Long id);
    boolean existsByUserIdAndActiveIsTrue(Long userId);

    boolean existsByVehicleModel(VehicleModel vehicleModel);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Announcement a set a.active = false where a.user.id = :userId")
    int deactivateByUserId(@Param("userId") Long userId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Announcement a set a.active = false where a.id = :announcementId")
    void deactivateByAnnouncementId(@Param("announcementId") Long announcementId);

    List<Announcement> findOtherUserAnnouncements(@Param("announcementId") Long announcementId, @Param("userId") Long userId);

    List<Announcement> findFirst10ByActiveIsTrueOrderByCreationDateDesc();

    List<Announcement> findFirst20ByActiveIsTrueOrderByCreationDateDesc();

    List<Announcement> findFirst5ByUserIdAndActiveIsTrueOrderByCreationDateDesc(Long userId);

}
