package com.app.common.validator;

import com.app.announcement.entity.ObservedAnnouncement;
import com.app.announcement.validator.ObservedAnnouncementValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ObservedAnnouncementValidatorTest {

    private final ObservedAnnouncementValidator observedAnnouncementValidator = new ObservedAnnouncementValidator();

    @Test
    public void shouldVerifyCorrectly() {
        //given
        ObservedAnnouncement observedAnnouncement = ObservedAnnouncement.builder().announcement(null).user(null).build();

        //when

        //then
    }
}
