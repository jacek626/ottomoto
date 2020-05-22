package com.app.searchform;

import com.app.entity.Announcement;
import com.app.entity.QAnnouncement;
import com.app.entity.QObservedAnnouncement;
import com.app.repository.AnnouncementRepository;
import com.app.repository.UserRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Component
public class ObservedAnnouncementSearchStrategyDecorator implements SearchStrategy<Announcement> {

    private final AnnouncementSearchStrategy announcementSearchFormStrategy;

    private final UserRepository userRepository;

    private final AnnouncementRepository announcementRepository;

    public ObservedAnnouncementSearchStrategyDecorator(AnnouncementSearchStrategy announcementSearchFormStrategy, UserRepository userRepository, AnnouncementRepository announcementRepository) {
        this.announcementSearchFormStrategy = announcementSearchFormStrategy;
        this.userRepository = userRepository;
        this.announcementRepository = announcementRepository;
    }

/*    @Override
    public Predicate preparePredicate(Announcement entity) {
        return announcementSearchFormStrategy.preparePredicate(entity);
    }*/

    @Override
    public Page<Announcement> loadData(PageRequest pageRequest, Predicate predicate) {
        BooleanBuilder booleanBuilder = new BooleanBuilder(predicate);

        booleanBuilder.and(QAnnouncement.announcement.id.eq(QObservedAnnouncement.observedAnnouncement.id));

        long userId = userRepository.findIdByLogin(getContext().getAuthentication().getName());
        booleanBuilder.and(QObservedAnnouncement.observedAnnouncement.user.id.eq(userId));

        return announcementRepository.findByPredicatesAndLoadMainPicture(pageRequest, booleanBuilder, QObservedAnnouncement.observedAnnouncement);
    }

    @Override
    public Map<String, Object> prepareDataForHtmlElements(Announcement entity) {
        return announcementSearchFormStrategy.prepareDataForHtmlElements(entity);
    }
}
