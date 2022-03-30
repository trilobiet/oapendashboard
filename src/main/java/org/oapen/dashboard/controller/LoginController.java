package org.oapen.dashboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, String error) {
    	
        if (error != null)
            model.addAttribute("error", "Your username and password are invalid.");

        return "login"; // src/main/resources/templates/login.html (thymeleaf)
    }
    
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(Model model) {

        model.addAttribute("msglogout", "You have been logged out successfully.");

        return "login"; // src/main/resources/templates/login.html (thymeleaf)
    }
}