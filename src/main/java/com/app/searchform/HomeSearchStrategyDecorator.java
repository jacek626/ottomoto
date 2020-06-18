package com.app.searchform;

import com.app.entity.Announcement;
import com.app.repository.AnnouncementRepository;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class HomeSearchStrategyDecorator implements SearchStrategy<Announcement> {

    private final AnnouncementSearchStrategy announcementSearchFormStrategy;

    private final AnnouncementRepository announcementRepository;

    public HomeSearchStrategyDecorator(AnnouncementSearchStrategy announcementSearchFormStrategy, AnnouncementRepository announcementRepository) {
        this.announcementSearchFormStrategy = announcementSearchFormStrategy;
        this.announcementRepository = announcementRepository;
    }

    @Override
    public Page<Announcement> loadData(PageRequest pageRequest, Predicate predicate) {
        List<Announcement> announcements = announcementRepository.findFirst20ByDeactivationDateIsNullOrderByCreationDateDesc();

        return new PageImpl<>(announcements);
    }

    @Override
    public Map<String, Object> prepareDataForHtmlElements(Announcement entity) {
        return announcementSearchFormStrategy.prepareDataForHtmlElements(entity);
    }
}


