package com.app.validator;

import com.app.entity.Announcement;
import com.app.enums.ValidatorCode;
import com.app.repository.AnnouncementRepository;
import com.app.utils.Result;
import com.app.utils.ValidationDetails;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
public class AnnouncementValidator {

	private AnnouncementRepository announcementRepository;

	public AnnouncementValidator(AnnouncementRepository announcementRepository) {
		this.announcementRepository = announcementRepository;
	}

	public AnnouncementValidator() {
	}

	public Result checkBeforeSave(Announcement announcement) {
		return Result.create(validateAnnouncementBeforeSave(announcement));
	}
	
	private Map<String, ValidationDetails> validateAnnouncementBeforeSave(Announcement announcement) {
		Map<String, ValidationDetails> validation = new HashMap<>();

		if (announcement.getUser() == null)
			validation.put("user", ValidationDetails.of(ValidatorCode.IS_EMPTY));
		else if (!announcement.getUser().getActive())
			validation.put("user", ValidationDetails.of(ValidatorCode.IS_DEACTIVATED));
		else if (announcement.getPrice().compareTo(BigDecimal.ZERO) < 0)
			validation.put("price", ValidationDetails.of(ValidatorCode.IS_NEGATIVE));

		
		return validation;
	}
	
	public Result checkBeforeDeactivate(Long announcementId) {
		return Result.create(validateAnnouncementBeforeDeactivate(announcementId));
	}
	
	private Map<String, ValidationDetails> validateAnnouncementBeforeDeactivate(Long announcementId) {
		Map<String, ValidationDetails> validation = new HashMap<>();

		boolean announcementToDeactivateExists = announcementRepository.existsByIdAndDeactivationDateIsNull(announcementId);
		
		if (!announcementToDeactivateExists)
			validation.put("announcement", ValidationDetails.of(ValidatorCode.IS_DEACTIVATED));
		
		return validation;
	}

}
