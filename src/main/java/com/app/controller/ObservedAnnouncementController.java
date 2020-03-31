package com.app.controller;

import com.app.entity.Announcement;
import com.app.enums.VehicleType;
import com.app.repository.UserRepository;
import com.app.searchForm.SearchFormStrategy;
import com.app.utils.PaginationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/observedAnnouncements")
public class ObservedAnnouncementController {

    private final SearchFormStrategy<Announcement> observedAnnouncementSearchFormStrategyDecorator;
    private final UserRepository userRepository;

    public ObservedAnnouncementController(SearchFormStrategy<Announcement> observedAnnouncementSearchFormStrategyDecorator, UserRepository userRepository) {
        this.observedAnnouncementSearchFormStrategyDecorator = observedAnnouncementSearchFormStrategyDecorator;
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

        //announcement.setUser(userRepository.findByLogin(getContext().getAuthentication().getName()));

        PaginationDetails paginationDetails = PaginationDetails.builder().page(page).size(size).orderBy(orderBy).sort(sort).build();

        model.addAllAttributes(observedAnnouncementSearchFormStrategyDecorator.prepareSearchForm(announcement, paginationDetails));

        return "/announcement/announcementList";
    }
}
