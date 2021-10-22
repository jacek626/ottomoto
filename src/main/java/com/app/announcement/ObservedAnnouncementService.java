package com.app.announcement;

import com.app.announcement.entity.ObservedAnnouncement;
import com.app.announcement.validator.ObservedAnnouncementValidator;
import com.app.common.enums.ValidatorCode;
import com.app.common.utils.validation.Result;
import com.app.common.utils.validation.ValidationDetails;
import org.springframework.beans.factory.annotation.Autowired;

public class ObservedAnnouncementService {

    @Autowired
    private ObservedAnnouncementRepository observedAnnouncementRepository;

    @Autowired
    private ObservedAnnouncementValidator observedAnnouncementValidator;

    public Result saveObservedAnnouncement(ObservedAnnouncement observedAnnouncement) {
        Result result = observedAnnouncementValidator.checkBeforeSave(observedAnnouncement);
        result.ifSuccess(() -> observedAnnouncementRepository.save(observedAnnouncement));

        return result;
    }

    public Result deleteObservedAnnouncement(ObservedAnnouncement observedAnnouncement) {
        final Result result = Result.success();

        observedAnnouncementRepository.findById(observedAnnouncement.getId()).ifPresentOrElse(
                e -> observedAnnouncementRepository.delete(e),
                () -> {
                    result.appendValidationResult("observedAnnouncement", ValidationDetails.of(ValidatorCode.NOT_EXISTS));
                }
        );

        return result;
    }
}
