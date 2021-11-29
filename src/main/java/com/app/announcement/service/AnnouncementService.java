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

	public Result saveAnnouncement(Announcement announcement) {
		Result result = announcementValidator.checkBeforeSave(announcement);

		setAnnouncementToPictures(announcement);

		if (result.isSuccess()) {
			announcementRepository.save(announcement);
		}

		return result;
	}

	private void setAnnouncementToPictures(Announcement announcement) {
		announcement.getPictures().stream().filter(e -> e.getAnnouncement() == null).forEach(picture -> picture.setAnnouncement(announcement));
	}

	public Result deactivateAnnouncement(Long announcementId) {
		Result result = announcementValidator.checkBeforeDeactivate(announcementId);

		result.ifSuccess(() -> announcementRepository.deactivateByAnnouncementId(announcementId));

		return result;
	}

}
