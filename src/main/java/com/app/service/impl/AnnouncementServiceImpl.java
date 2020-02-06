package com.app.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.entity.Announcement;
import com.app.entity.Picture;
import com.app.repository.AnnouncementRepository;
import com.app.service.AnnouncementService;
import com.app.utils.Result;
import com.app.validator.AnnouncementValidator;


@Service("announcementService")
public class AnnouncementServiceImpl implements AnnouncementService {
	
	@Autowired
	private AnnouncementRepository announcementRepository;

	@Autowired
	private AnnouncementValidator announcementValidator;
	
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

	//@Override
	//public int coutByVehicleModelManufacturerId(Long manufacturerId) {
	//	return announcementRepository.coutByVehicleModelManufacturerId(manufacturerId);
	//}

}
