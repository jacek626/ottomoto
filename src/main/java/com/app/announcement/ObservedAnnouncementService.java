package com.app.announcement;

import com.app.announcement.entity.ObservedAnnouncement;
import com.app.announcement.validator.ObservedAnnouncementValidator;
import com.app.common.enums.ValidatorCode;
import com.app.common.utils.validation.Result;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.app.common.utils.validation.ValidationDetails.*;

@AllArgsConstructor
@Service
public class ObservedAnnouncementService {
    private final ObservedAnnouncementRepository observedAnnouncementRepository;
    private final ObservedAnnouncementValidator observedAnnouncementValidator;

    public Result saveObservedAnnouncement(ObservedAnnouncement observedAnnouncement) {
        Result result = observedAnnouncementValidator.checkBeforeSave(observedAnnouncement);
        result.ifSuccess(() -> observedAnnouncementRepository.save(observedAnnouncement));

        return result;
    }

    public Result deleteObservedAnnouncement(ObservedAnnouncement observedAnnouncement) {
        final Result result = Result.success();

        observedAnnouncementRepository.findById(observedAnnouncement.getId())
                .ifPresentOrElse(observedAnnouncementRepository::delete,
                    () -> result.addValidationResult("observedAnnouncement", of(ValidatorCode.NOT_EXISTS))
        );

        return result;
    }
}
