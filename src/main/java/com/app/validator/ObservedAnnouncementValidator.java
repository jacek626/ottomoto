package com.app.validator;

import com.app.entity.ObservedAnnouncement;
import com.app.enums.ValidatorCode;
import com.app.utils.Result;
import com.app.utils.ValidationDetails;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ObservedAnnouncementValidator {
	
	public Result checkBeforeSaveObservedAnnouncement(ObservedAnnouncement observedAnnouncement) {
		return Result.create(validateObservedAnnouncementBeforeSave(observedAnnouncement));
	}

	private Map<String, ValidationDetails> validateObservedAnnouncementBeforeSave(ObservedAnnouncement observedAnnouncement) {
		Map<String, ValidationDetails> validation = new HashMap<>();

		if (observedAnnouncement.getUser() == null)
			validation.put("user", ValidationDetails.of(ValidatorCode.IS_EMPTY));
		else if (!observedAnnouncement.getUser().getActive())
			validation.put("user", ValidationDetails.of(ValidatorCode.IS_DEACTIVATED));
		else if (observedAnnouncement.getAnnouncement() == null)
			validation.put("announcement", ValidationDetails.of(ValidatorCode.IS_EMPTY));
		else if (observedAnnouncement.getAnnouncement().getDeactivationDate() != null)
			validation.put("announcement", ValidationDetails.of(ValidatorCode.IS_DEACTIVATED));

		return validation;
	}
}
