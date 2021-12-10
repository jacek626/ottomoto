package com.app.controller;

import com.app.announcement.dto.AnnouncementDto;
import com.app.announcement.entity.Announcement;
import com.app.common.utils.search.PaginationDetails;
import com.app.searchform.SearchStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    private final SearchStrategy<Announcement, AnnouncementDto> announcementSearchStrategy;

    public HomeController(SearchStrategy<Announcement, AnnouncementDto> announcementSearch) {
        this.announcementSearchStrategy = announcementSearch;
    }

    @RequestMapping("/")
    public String home(Model model, @ModelAttribute("announcement") Announcement announcement) {
        PaginationDetails paginationDetails = PaginationDetails.builder().page(1).size(16).orderBy("id").sort("DESC").build();
        model.addAllAttributes(announcementSearchStrategy.prepareSearchForm(announcement, paginationDetails));

        return "home";
    }

}
