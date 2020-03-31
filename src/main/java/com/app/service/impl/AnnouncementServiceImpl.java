package com.app.service.impl;

import com.app.entity.Announcement;
import com.app.repository.AnnouncementRepository;
import com.app.service.AnnouncementService;
import com.app.service.PictureService;
import com.app.utils.Result;
import com.app.validator.AnnouncementValidator;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service("announcementService")
public class AnnouncementServiceImpl implements AnnouncementService {
	
	private final AnnouncementRepository announcementRepository;

	private final AnnouncementValidator announcementValidator;

	private final PictureService pictureService;

	public AnnouncementServiceImpl(AnnouncementRepository announcementRepository, AnnouncementValidator announcementValidator, PictureService pictureService) {
		this.announcementRepository = announcementRepository;
		this.announcementValidator = announcementValidator;
		this.pictureService = pictureService;
	}

	@Override
	public Result saveAnnouncement(Announcement announcement) {
		Result result = announcementValidator.checkBeforeSave(announcement);
		
		if(result.isSuccess()) {
			announcementRepository.save(announcement);
		}
		
		return result;
	}

	@Override
	public long activateAnnouncement(Long announcementId) {
		return announcementRepository.updateDeactivationDateByUserId(null, announcementId);
	}

	@Override
	public Result deactivateAnnouncement(Long announcementId) {
		Result result = announcementValidator.checkBeforeDeactivate(announcementId);
		
		if(result.isSuccess()) {
			announcementRepository.updateDeactivationDateByAnnouncementId(new Date(), announcementId);
		}
		
		return result;
	}

	@Override
	public long deactivateAllUserAnnouncements(Long userId) {
		return announcementRepository.updateDeactivationDateByUserId(null, userId);
	}

}
