package com.app.announcement.validator;

import com.app.announcement.entity.Announcement;
import com.app.announcement.entity.ObservedAnnouncement;
import com.app.common.types.ValidatorCode;
import com.app.common.utils.validation.Result;
import com.app.common.utils.validation.ValidationDetails;
import com.app.common.validator.Validation;
import com.app.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ObservedAnnouncementValidator implements Validation<ObservedAnnouncement> {

	@Override
	public Result<ObservedAnnouncement> validateForSave(ObservedAnnouncement observedAnnouncement) {
		var errors = createErrorsContainer();

		errors.putAll(checkUserIsSetAndNotEmpty(observedAnnouncement.getUser()));
		errors.putAll(checkAnnouncementIsSetAndNotEmpty(observedAnnouncement.getAnnouncement()));

		return Result.create(errors).setValidatedObject(observedAnnouncement);
	}

	@Override
	public Result<ObservedAnnouncement> validateForDelete(ObservedAnnouncement objectToValidate) {
		throw new UnsupportedOperationException();
	}

	private Map<String, ValidationDetails> checkUserIsSetAndNotEmpty(User user) {
		var errors = createErrorsContainer();

		if (user == null) {
			errors.put("user", ValidationDetails.of(ValidatorCode.IS_EMPTY));
		}
		else if (!user.getActive()) {
			errors.put("user", ValidationDetails.of(ValidatorCode.IS_DEACTIVATED));
		}

		return errors;
	}

	private Map<String, ValidationDetails> checkAnnouncementIsSetAndNotEmpty(Announcement announcement) {
		var errors = createErrorsContainer();

		if (announcement == null)
			errors.put("announcement", ValidationDetails.of(ValidatorCode.IS_EMPTY));
		else if (!announcement.getActive())
			errors.put("announcement", ValidationDetails.of(ValidatorCode.IS_DEACTIVATED));

		return errors;
	}
}
