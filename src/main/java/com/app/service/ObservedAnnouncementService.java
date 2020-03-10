package com.app.service;

import com.app.entity.ObservedAnnouncement;
import com.app.utils.Result;

public interface ObservedAnnouncementService {
    Result saveObservedAnnouncement(ObservedAnnouncement observedAnnouncement);
    Result deleteObservedAnnouncement(ObservedAnnouncement observedAnnouncement);
}
