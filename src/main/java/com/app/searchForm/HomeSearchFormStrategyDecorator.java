package com.app.searchForm;

import com.app.entity.Announcement;
import com.app.repository.AnnouncementRepository;
import com.app.utils.PredicatesAndUrlParams;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class HomeSearchFormStrategyDecorator implements SearchFormStrategy<Announcement> {

    private final AnnouncementSearchFormStrategy announcementSearchFormStrategy;

    private final AnnouncementRepository announcementRepository;

    public HomeSearchFormStrategyDecorator(AnnouncementSearchFormStrategy announcementSearchFormStrategy, AnnouncementRepository announcementRepository) {
        this.announcementSearchFormStrategy = announcementSearchFormStrategy;
        this.announcementRepository = announcementRepository;
    }

    @Override
    public PredicatesAndUrlParams preparePredicatesAndUrlParams(Announcement entity) {
        return announcementSearchFormStrategy.preparePredicatesAndUrlParams(entity);
    }

    @Override
    public Page<Announcement> loadData(PageRequest pageRequest, List<Predicate> predicates) {
        List<Announcement> announcements = announcementRepository.findFirst20ByDeactivationDateIsNullOrderByCreationDateDesc();

        return new PageImpl<>(announcements);
    }

    @Override
    public Map<String, Object> prepareDataForHtmlElements(Announcement entity) {
        return announcementSearchFormStrategy.prepareDataForHtmlElements(entity);
    }
}


