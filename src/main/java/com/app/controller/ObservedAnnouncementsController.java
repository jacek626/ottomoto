package com.app.controller;

import com.app.dto.AnnouncementDto;
import com.app.entity.Announcement;
import com.app.entity.ObservedAnnouncement;
import com.app.entity.User;
import com.app.repository.AnnouncementRepository;
import com.app.repository.ObservedAnnouncementRepository;
import com.app.repository.UserRepository;
import com.app.searchform.SearchStrategy;
import com.app.utils.search.PaginationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Controller
@RequestMapping("/observedAnnouncements")
public class ObservedAnnouncementsController {

    private final SearchStrategy<Announcement, AnnouncementDto> observedAnnouncementSearchStrategyDecorator;
    private final UserRepository userRepository;
    private final ObservedAnnouncementRepository observedAnnouncementRepository;
    private final AnnouncementRepository announcementRepository;

    public ObservedAnnouncementsController(SearchStrategy<Announcement, AnnouncementDto> observedAnnouncementSearchStrategyDecorator, UserRepository userRepository, ObservedAnnouncementRepository observedAnnouncementRepository, AnnouncementRepository announcementRepository) {
        this.observedAnnouncementSearchStrategyDecorator = observedAnnouncementSearchStrategyDecorator;
        this.userRepository = userRepository;
        this.observedAnnouncementRepository = observedAnnouncementRepository;
        this.announcementRepository = announcementRepository;
    }

    @RequestMapping
    public String list(@RequestParam(name = "page", defaultValue = "1", required = false) int page,
                       @RequestParam(name = "size", required = false, defaultValue = "10") int size,
                       @RequestParam(name = "orderBy", required = false, defaultValue = "id") String orderBy,
                       @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort,
                       @ModelAttribute("announcement") Announcement announcement,
                       Model model) {
   //     model.addAttribute("requestMapping", "observed");

        PaginationDetails paginationDetails = PaginationDetails.builder().page(page).size(size).orderBy(orderBy).sort(sort).build();
        announcement.setUser(userRepository.findByLogin(getContext().getAuthentication().getName()));
        model.addAllAttributes(observedAnnouncementSearchStrategyDecorator.prepareSearchForm(announcement, paginationDetails));

        return "announcement/announcementList";
    }

    @RequestMapping(value = "toggleAnnouncementIsObserved", method = RequestMethod.GET)
    public @ResponseBody
    boolean toggleAnnouncementIsObserved(@RequestParam("userLogin") String userLogin, @RequestParam("announcementId") long announcementId) {
        if (observedAnnouncementRepository.existsByUserLoginAndAnnouncement(userLogin, announcementId)) {
            observedAnnouncementRepository.deleteByUserIdAndAnnouncementId(userRepository.findIdByLogin(userLogin), announcementId);
            return false;
        } else {
            User user = userRepository.findByLogin(userLogin);
            Optional<Announcement> announcement = announcementRepository.findById(announcementId);
            observedAnnouncementRepository.save(new ObservedAnnouncement(announcement.get(), user));

            return true;
        }
    }
}
