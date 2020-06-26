package com.app.controller;

import com.app.entity.Announcement;
import com.app.searchform.SearchStrategy;
import com.app.utils.PaginationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
	
	private final SearchStrategy homeSearchStrategyDecorator;

    public HomeController(SearchStrategy homeSearchStrategyDecorator) {
        this.homeSearchStrategyDecorator = homeSearchStrategyDecorator;
    }

    @RequestMapping("/")
    public String home(Model model, @ModelAttribute("announcement")  Announcement announcement) {
    //	announcement.prepareFieldsForSearch();

        PaginationDetails paginationDetails = PaginationDetails.builder().page(1).size(1).orderBy("ID").sort("ASC").build();
        model.addAllAttributes(homeSearchStrategyDecorator.prepareSearchForm(announcement, paginationDetails));

        return "home";
    }
}
