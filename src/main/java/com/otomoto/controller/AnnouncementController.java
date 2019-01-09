package com.otomoto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.otomoto.entity.Announcement;
import com.otomoto.entity.User;
import com.otomoto.repository.AnnouncementRepository;
import com.otomoto.repository.UserRepository;

@Controller
@RequestMapping("announcement/")
public class AnnouncementController {
	
	@Autowired
	AnnouncementRepository announcementRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@RequestMapping(value="add",method=RequestMethod.GET)
	public String register(Model model) {
		
		model.addAttribute("announcement", new Announcement());
		
		return "announcement/edit";
	}
	
	
	@RequestMapping(value="save",method=RequestMethod.POST)
	public String registerPost(@ModelAttribute("announcement") Announcement announcement,Model model,
			Authentication authentication) {
		
		if(announcement.getId() == null) {
			announcement.setUser(userRepository.findByLogin(authentication.getName()));
			announcementRepository.save(announcement);
			//System.out.println("LISTA>>>>>");
			//userRepository.findByLogin(authentication.getName()).getAnnouncementList().stream().
			//forEach(a -> System.out.println("ID " + a.getId()));
		}
		else {
		//	announcementRepository.update(announcement);
			announcementRepository.save(announcement);
		}
			
		//return "redirect:/announcement/edit/"+ announcement.getId();
		return "redirect:/announcement/list";
	}
	
	@RequestMapping(value="edit/{id}",method=RequestMethod.GET)
	public String edit(@PathVariable("id") Long id, Model model) {
		
		model.addAttribute("announcement", announcementRepository.findById(id).get());
		
		return "announcement/edit";
	}
	
	@RequestMapping(value="/list")
	public String list(Model model) {
		model.addAttribute("announcementList", announcementRepository.findAll());
		
		return "/announcement/announcementList";
	}

}
