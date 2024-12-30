package com.example.hrms.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String showDashboard(Model model, Authentication authentication) {
        String username = "";
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername();
        }
        model.addAttribute("username", username);
        return "dashboard/dashboard";
    }
    
    @GetMapping("/messages")
    public String showMessages(Model model, Authentication authentication) {
        String username = "";
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername();
        }
        model.addAttribute("username", username);
        return "dashboard/messages";
    }
    
    @GetMapping("/mail")
    public String showMail(Model model, Authentication authentication) {
        String username = "";
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername();
        }
        model.addAttribute("username", username);
        return "dashboard/mail";
    }
}