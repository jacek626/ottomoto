package com.app.service;

import com.app.entity.Announcement;
import com.app.repository.AnnouncementRepository;
import com.app.utils.Result;
import com.app.validator.AnnouncementValidator;
import org.springframework.stereotype.Service;

import java.util.Date;


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

	public long activateAnnouncement(Long announcementId) {
		return announcementRepository.updateDeactivationDateByUserId(null, announcementId);
	}

	public Result deactivateAnnouncement(Long announcementId) {
		Result result = announcementValidator.checkBeforeDeactivate(announcementId);
		
		if(result.isSuccess()) {
			announcementRepository.updateDeactivationDateByAnnouncementId(new Date(), announcementId);
		}
		
		return result;
	}

	public long deactivateAllUserAnnouncements(Long userId) {
		return announcementRepository.updateDeactivationDateByUserId(null, userId);
	}

}
