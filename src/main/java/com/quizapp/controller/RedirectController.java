package com.quizapp.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedirectController {

    @GetMapping("/redirect")
    public String redirect(Authentication auth) {

        String role = auth.getAuthorities().iterator().next().getAuthority();

        if (role.equals("ROLE_ADMIN")) {
            return "redirect:/dashboard"; // admin dashboard
        } else {
            return "redirect:/user_dashboard"; // user dashboard
        }
    }
}