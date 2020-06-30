package com.app.controller;

import com.app.dto.AnnouncementDto;
import com.app.entity.Announcement;
import com.app.entity.ObservedAnnouncement;
import com.app.entity.User;
import com.app.repository.AnnouncementRepository;
import com.app.repository.ObservedAnnouncementRepository;
import com.app.repository.UserRepository;
import com.app.searchform.SearchStrategy;
import com.app.utils.AnnouncementMapper;
import com.app.utils.PaginationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/observedAnnouncements")
public class ObservedAnnouncementsController {

    private final SearchStrategy<Announcement, AnnouncementDto> observedAnnouncementSearchStrategyDecorator;
    private final UserRepository userRepository;
    private final ObservedAnnouncementRepository observedAnnouncementRepository;
    private final AnnouncementRepository announcementRepository;
    private final AnnouncementMapper announcementMapper;

    public ObservedAnnouncementsController(SearchStrategy<Announcement, AnnouncementDto> observedAnnouncementSearchStrategyDecorator, UserRepository userRepository, ObservedAnnouncementRepository observedAnnouncementRepository, AnnouncementRepository announcementRepository, AnnouncementMapper announcementMapper) {
        this.observedAnnouncementSearchStrategyDecorator = observedAnnouncementSearchStrategyDecorator;
        this.userRepository = userRepository;
        this.observedAnnouncementRepository = observedAnnouncementRepository;
        this.announcementRepository = announcementRepository;
        this.announcementMapper = announcementMapper;
    }

    @RequestMapping
    public String list(@RequestParam(name = "page", defaultValue = "1", required = false) int page,
                       @RequestParam(name = "size", required = false, defaultValue = "10") int size,
                       @RequestParam(name = "orderBy", required = false, defaultValue = "id") String orderBy,
                       @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort,
                       @ModelAttribute("announcement") AnnouncementDto announcementDto,
                       Model model) {
        model.addAttribute("requestMapping", "list");

        PaginationDetails paginationDetails = PaginationDetails.builder().page(page).size(size).orderBy(orderBy).sort(sort).build();
        model.addAllAttributes(observedAnnouncementSearchStrategyDecorator.prepareSearchForm(announcementMapper.convertToEntity(announcementDto), paginationDetails));

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
