package com.app.service;

import com.app.entity.Announcement;
import com.app.utils.Result;

public interface AnnouncementService {
	Result saveAnnouncement(Announcement announcement);

	long activateAnnouncement(Long announcementId);
	Result deactivateAnnouncement(Long announcementId);
	long deactivateAllUserAnnouncements(Long userId);
}