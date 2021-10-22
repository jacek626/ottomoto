package com.app.announcement.validator;

import com.app.announcement.entity.Announcement;
import com.app.announcement.entity.ObservedAnnouncement;
import com.app.common.enums.ValidatorCode;
import com.app.common.utils.validation.Result;
import com.app.common.utils.validation.ValidationDetails;
import com.app.common.validator.ValidatorCommonMethods;
import com.app.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ObservedAnnouncementValidator implements ValidatorCommonMethods<ObservedAnnouncement> {

	@Override
	public Result checkBeforeSave(ObservedAnnouncement observedAnnouncement) {
		var errors = createErrorMap();

		errors.putAll(checkUserIsSetAndNotEmpty(observedAnnouncement.getUser()));
		errors.putAll(checkAnnouncementIsSetAndNotEmpty(observedAnnouncement.getAnnouncement()));

		return Result.create(errors);
	}

	private Map<String, ValidationDetails> checkUserIsSetAndNotEmpty(User user) {
		var errors = createErrorMap();

		if (user == null)
			errors.put("user", ValidationDetails.of(ValidatorCode.IS_EMPTY));
		else if (!user.getActive())
			errors.put("user", ValidationDetails.of(ValidatorCode.IS_DEACTIVATED));

		return errors;
	}

	private Map<String, ValidationDetails> checkAnnouncementIsSetAndNotEmpty(Announcement announcement) {
		var errors = createErrorMap();

		if (announcement == null)
			errors.put("announcement", ValidationDetails.of(ValidatorCode.IS_EMPTY));
		else if (!announcement.getActive())
			errors.put("announcement", ValidationDetails.of(ValidatorCode.IS_DEACTIVATED));

		return errors;
	}


	@Override
	public Result checkBeforeDelete(ObservedAnnouncement objectToValidate) {
		throw new UnsupportedOperationException();
	}
}
