package com.app.controller;

import com.app.dto.AnnouncementDto;
import com.app.entity.Announcement;
import com.app.searchform.SearchStrategy;
import com.app.utils.search.PaginationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    private final SearchStrategy<Announcement, AnnouncementDto> announcementSearchStrategy;

    public HomeController(SearchStrategy<Announcement, AnnouncementDto> announcementSearchStrategy) {
        this.announcementSearchStrategy = announcementSearchStrategy;
    }

    @RequestMapping("/")
    public String home(Model model, @ModelAttribute("announcement") Announcement announcement) {
        PaginationDetails paginationDetails = PaginationDetails.builder().page(1).size(16).orderBy("id").sort("DESC").build();
        model.addAllAttributes(announcementSearchStrategy.prepareSearchForm(announcement, paginationDetails));

        return "home";
    }

}
