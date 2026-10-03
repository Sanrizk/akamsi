package com.akamsi.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login"; // Renders login.html
    }
    
    @GetMapping("/")
    public String index(Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }
        
        for (GrantedAuthority auth : authentication.getAuthorities()) {
            String role = auth.getAuthority();
            if (role.equals("ROLE_ADMIN")) {
                return "redirect:/admin/dashboard";
            } else if (role.equals("ROLE_GURU")) {
                return "redirect:/guru/dashboard";
            } else if (role.equals("ROLE_SISWA")) {
                return "redirect:/siswa/dashboard";
            }
        }
        
        return "redirect:/login"; 
    }
}
