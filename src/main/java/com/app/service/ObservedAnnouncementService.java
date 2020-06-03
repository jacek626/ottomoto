package com.app.service;

import com.app.entity.ObservedAnnouncement;
import com.app.enums.ValidatorCode;
import com.app.repository.ObservedAnnouncementRepository;
import com.app.utils.Result;
import com.app.utils.ValidationDetails;
import com.app.validator.ObservedAnnouncementValidator;
import org.springframework.beans.factory.annotation.Autowired;

public class ObservedAnnouncementService {

    @Autowired
    private ObservedAnnouncementRepository observedAnnouncementRepository;

    @Autowired
    private ObservedAnnouncementValidator observedAnnouncementValidator;

    public Result saveObservedAnnouncement(ObservedAnnouncement observedAnnouncement) {
        Result result = observedAnnouncementValidator.checkBeforeSaveObservedAnnouncement(observedAnnouncement);

        if(result.isSuccess()) {
            observedAnnouncementRepository.save(observedAnnouncement);
        }

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
