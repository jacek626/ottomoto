package com.app.announcement.validator;

import com.app.announcement.entity.Announcement;
import com.app.announcement.repository.AnnouncementRepository;
import com.app.common.enums.ValidatorCode;
import com.app.common.utils.validation.Result;
import com.app.common.utils.validation.ValidationDetails;
import com.app.common.validator.ValidatorCommonMethods;
import com.app.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static com.app.common.utils.validation.ValidationDetails.*;

@AllArgsConstructor
@NoArgsConstructor
@Component
public class AnnouncementValidator implements ValidatorCommonMethods<Announcement> {

    private AnnouncementRepository announcementRepository;

    @Override
    public Result checkBeforeSave(Announcement announcement) {
        var errors = createErrorsContainer();

        errors.putAll(checkUserIsSet(announcement.getUser()));
        errors.putAll(checkUserIsActive(announcement.getUser()));
        errors.putAll(checkPriceIsGraterThanZero(announcement.getPrice()));

        return Result.create(errors);
    }

    @Override
    public Result checkBeforeDelete(Announcement objectToValidate) {
        throw new NotImplementedException("Not implemented");
    }

    private Map<String, ValidationDetails> checkUserIsSet(User user) {
        var errors = createErrorsContainer();

        if (user == null) {
            errors.put("user", of(ValidatorCode.IS_EMPTY));
        }

        return errors;
    }

    private Map<String, ValidationDetails> checkUserIsActive(User user) {
        var errors = createErrorsContainer();

        if (user != null && !user.getActive())
            errors.put("user", of(ValidatorCode.IS_DEACTIVATED));

        return errors;
    }

    private Map<String, ValidationDetails> checkPriceIsGraterThanZero(BigDecimal price) {
        var errors = createErrorsContainer();

        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            errors.put("price", of(ValidatorCode.IS_NEGATIVE));
        }

        return errors;
    }

    public Result checkBeforeDeactivate(Long announcementId) {
        return Result.create(validateAnnouncementBeforeDeactivate(announcementId));
    }

    private Map<String, ValidationDetails> validateAnnouncementBeforeDeactivate(Long announcementId) {
        Map<String, ValidationDetails> validation = new HashMap<>();

        boolean announcementToDeactivateExists = announcementRepository.existsByUserIdAndActiveIsTrue(announcementId);

        if (!announcementToDeactivateExists) {
            validation.put("announcement", of(ValidatorCode.IS_DEACTIVATED));
        }

        return validation;
    }
}
