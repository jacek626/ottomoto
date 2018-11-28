package com.otomoto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("announcement/")
public class AnnouncementController {
	
	@RequestMapping("add")
	public String register() {
		
		
		return "announcement/edit";
	}

}
