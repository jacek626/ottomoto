package com.app.validator;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.entity.Announcement;
import com.app.entity.Manufacturer;
import com.app.repository.AnnouncementRepository;
import com.app.utils.Result;

@Component
public class AnnouncementValidator {
	
	@Autowired
	private AnnouncementRepository announcementRepository;

	public Result checkBeforeSave(Announcement announcement) {
		return Result.create(validateManufacturerBeforeSave(announcement));
	}
	
	Map<String, String> validateManufacturerBeforeSave(Announcement announcement) { 
		Map<String, String> validation = new HashMap<String, String>();

		if (announcement.getUser() == null)
			validation.put("user", "isEmpty");
		else if (!announcement.getUser().getActive())
			validation.put("user", "isDeactivated");
		else if (announcement.getPrice().compareTo(BigDecimal.ZERO) < 0)
			validation.put("price", "isNegative");
		
		return validation;
	}
	
	public Result checkBeforeDeactivate(Long announcementId) {
		return Result.create(validateAnnouncemenetBeforeDeactivate(announcementId));
	}
	
	Map<String, String> validateAnnouncemenetBeforeDeactivate(Long announcementId) { 
		Map<String, String> validation = new HashMap<String, String>();

		boolean announcementToDeactivateExists = announcementRepository.existsByIdAndDeactivationDateIsNull(announcementId);
		
		if (!announcementToDeactivateExists)
			validation.put("announcement", "isAlreadyDeactivated");
		
		return validation;
	}
	
	/*
	 * @Override public boolean checkUserContainsActiveAnnouncements(Long userId) {
	 * return
	 * announcementRepository.existsByUserIdAndDeactivationDateIsNull(userId); } }
	 */
}
