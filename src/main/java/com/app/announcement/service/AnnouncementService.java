package com.app.announcement.service;

import com.app.announcement.entity.Announcement;
import com.app.announcement.repository.AnnouncementRepository;
import com.app.announcement.validator.AnnouncementValidator;
import com.app.common.utils.validation.Result;
import org.springframework.stereotype.Service;


@Service("announcementService")
public class AnnouncementService {
	
	private final AnnouncementRepository announcementRepository;

	private final AnnouncementValidator announcementValidator;


	public AnnouncementService(AnnouncementRepository announcementRepository, AnnouncementValidator announcementValidator) {
		this.announcementRepository = announcementRepository;
		this.announcementValidator = announcementValidator;
	}

	public Result<Announcement> saveAnnouncement(Announcement announcement) {
		var result = announcementValidator.validateForSave(announcement);
		setAnnouncementToPictures(announcement);
		result.ifSuccess(() -> announcementRepository.save(announcement));

		return result;
	}

	public Result<Announcement> deactivateAnnouncement(Long announcementId) {
		return announcementValidator.checkBeforeDeactivate(announcementId)
				.ifSuccess(() -> announcementRepository.deactivateByAnnouncementId(announcementId));
	}

	private void setAnnouncementToPictures(Announcement announcement) {
		announcement.getPictures().stream().filter(e -> e.getAnnouncement() == null).forEach(picture -> picture.setAnnouncement(announcement));
	}

}
