package com.app.picture.repository;

import com.app.picture.entity.Picture;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PictureRepository extends CrudRepository<Picture, Long > {
    @Query(value="select p.miniatureRepositoryName as miniatureRepositoryName, p.announcement.id as announcementId from Picture p where p.mainPhotoInAnnouncement = true and p.announcement.id in (:announcementsIds)")
    List<PictureProjection> findMainPhotosByAnnouncementId(@Param("announcementsIds") Collection<Long> announcementsIds);
}
