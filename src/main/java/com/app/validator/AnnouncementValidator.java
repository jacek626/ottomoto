package com.app.validator;

import com.app.entity.Announcement;
import com.app.entity.User;
import com.app.enums.ValidatorCode;
import com.app.repository.AnnouncementRepository;
import com.app.utils.Result;
import com.app.utils.ValidationDetails;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
public class AnnouncementValidator implements ValidatorCommonMethods<Announcement> {

    private AnnouncementRepository announcementRepository;

    public AnnouncementValidator(AnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }

    public AnnouncementValidator() {
    }

    @Override
    public Result checkBeforeSave(Announcement announcement) {
        var errors = createErrorMap();

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
        var errors = createErrorMap();

        if (user == null)
            errors.put("user", ValidationDetails.of(ValidatorCode.IS_EMPTY));

        return errors;
    }

    private Map<String, ValidationDetails> checkUserIsActive(User user) {
        var errors = createErrorMap();

        if (user != null && !user.getActive())
            errors.put("user", ValidationDetails.of(ValidatorCode.IS_DEACTIVATED));

        return errors;
    }

    private Map<String, ValidationDetails> checkPriceIsGraterThanZero(BigDecimal price) {
        var errors = createErrorMap();

        if (price == null || price.compareTo(BigDecimal.ZERO) < 0)
            errors.put("price", ValidationDetails.of(ValidatorCode.IS_NEGATIVE));

        return errors;
    }

    public Result checkBeforeDeactivate(Long announcementId) {
        return Result.create(validateAnnouncementBeforeDeactivate(announcementId));
    }

    private Map<String, ValidationDetails> validateAnnouncementBeforeDeactivate(Long announcementId) {
        Map<String, ValidationDetails> validation = new HashMap<>();

        boolean announcementToDeactivateExists = announcementRepository.existsByUserIdAndActiveIsTrue(announcementId);

        if (!announcementToDeactivateExists)
            validation.put("announcement", ValidationDetails.of(ValidatorCode.IS_DEACTIVATED));

        return validation;
    }
}
