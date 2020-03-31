package com.app.searchForm;

import com.app.entity.Announcement;
import com.app.entity.QAnnouncement;
import com.app.entity.QObservedAnnouncement;
import com.app.repository.AnnouncementRepository;
import com.app.repository.ObservedAnnouncementRepository;
import com.app.repository.UserRepository;
import com.app.utils.PredicatesAndUrlParams;
import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Component
public class ObservedAnnouncementSearchFormStrategyDecorator implements SearchFormStrategy<Announcement> {

    private final AnnouncementSearchFormStrategy announcementSearchFormStrategy;

    private final UserRepository userRepository;

    @Autowired
    private AnnouncementRepository announcementRepository;

    public ObservedAnnouncementSearchFormStrategyDecorator(AnnouncementSearchFormStrategy announcementSearchFormStrategy, ObservedAnnouncementRepository observedAnnouncementRepository, UserRepository userRepository) {
        this.announcementSearchFormStrategy = announcementSearchFormStrategy;
        this.userRepository = userRepository;
    }

    @Override
    public PredicatesAndUrlParams preparePredicatesAndUrlParams(Announcement entity) {
        return announcementSearchFormStrategy.preparePredicatesAndUrlParams(entity);
    }

    @Override
    public Page<Announcement> loadData(PageRequest pageRequest, List<Predicate> predicates) {
        predicates.add(QAnnouncement.announcement.id.eq(QObservedAnnouncement.observedAnnouncement.id));

        long userId = userRepository.findIdByLogin(getContext().getAuthentication().getName());
        predicates.add(QObservedAnnouncement.observedAnnouncement.user.id.eq(userId));

        return announcementRepository.findByPredicatesAndLoadMainPicture(pageRequest, predicates, QObservedAnnouncement.observedAnnouncement);
    }

    @Override
    public Map<String, Object> prepareDataForHtmlElements(Announcement entity) {
        return announcementSearchFormStrategy.prepareDataForHtmlElements(entity);
    }
}
