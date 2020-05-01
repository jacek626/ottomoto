package com.app.controller;

import com.app.entity.Announcement;
import com.app.enums.VehicleType;
import com.app.repository.UserRepository;
import com.app.searchform.SearchStrategy;
import com.app.utils.PaginationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Controller
@RequestMapping("/observedAnnouncements")
public class ObservedAnnouncementsController {

    private final SearchStrategy<Announcement> observedAnnouncementSearchStrategyDecorator;
    private final UserRepository userRepository;

    public ObservedAnnouncementsController(SearchStrategy<Announcement> observedAnnouncementSearchStrategyDecorator, UserRepository userRepository) {
        this.observedAnnouncementSearchStrategyDecorator = observedAnnouncementSearchStrategyDecorator;
        this.userRepository = userRepository;
    }

    @RequestMapping
    public String list(@RequestParam(name = "page", defaultValue = "1",  required = false) int page,
                       @RequestParam(name = "size", required = false, defaultValue = "10") int size,
                       @RequestParam(name = "orderBy", required = false, defaultValue = "id") String orderBy,
                       @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort,
                       @ModelAttribute("announcement") Announcement announcement,
                       Model model) {

        model.addAttribute("requestMapping", "list");

        if(announcement.getVehicleType() == null)
            announcement.setVehicleType(VehicleType.CAR);

        announcement.setUser(userRepository.findByLogin(getContext().getAuthentication().getName()));

        PaginationDetails paginationDetails = PaginationDetails.builder().page(page).size(size).orderBy(orderBy).sort(sort).build();

        model.addAllAttributes(observedAnnouncementSearchStrategyDecorator.prepareSearchForm(announcement, paginationDetails));

        return "/announcement/announcementList";
    }
}
