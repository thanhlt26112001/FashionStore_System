package com.example.fashionstore_system.controller;

import com.example.fashionstore_system.entity.User;
import com.example.fashionstore_system.service.MailService;
import com.example.fashionstore_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AppController {
    @Autowired
    private UserService userService;
    @Autowired
    private MailService mailService;

    @GetMapping("/login")
    public String loginPage() {
        //prevent user return back to login page if they already login to the system
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }
        return "redirect:/";
    }

    @GetMapping("/process_register")
    public String createNewUser(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "sign_up";
    }

    @RequestMapping("/forgot_password")
    public String forgotPassword() {
        //prevent user return back to fotgot password page if they already login to the system
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "forgotPassword";
        }
        return "redirect:/";
    }

    @RequestMapping("/send_pass_back")
    public String sendPassword(@RequestParam(name = "email") String email) {
        mailService.sendSimpleMessage(email,"Reset password", "hello");
        return "redirect:/";
    }
}
