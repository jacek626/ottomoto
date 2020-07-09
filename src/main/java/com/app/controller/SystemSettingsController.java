package com.app.controller;

import com.app.utils.email.SystemEmail;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("system/")
public class SystemSettingsController {

    private final SystemEmail systemEmail;

    public SystemSettingsController(SystemEmail systemEmail) {
        this.systemEmail = systemEmail;
    }

    @RequestMapping(value = "settings")
    public String Settings(Model model) {
        model.addAttribute(systemEmail);

        return "system/settings";
    }

	@RequestMapping(value = "home")
    public String home() {

        return "system/home";
    }
}
