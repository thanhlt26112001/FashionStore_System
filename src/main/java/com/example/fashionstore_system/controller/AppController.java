package com.example.fashionstore_system.controller;

import com.example.fashionstore_system.config.Utility;
import com.example.fashionstore_system.entity.Customer;
import com.example.fashionstore_system.entity.User;
import com.example.fashionstore_system.jwt.JwtTokenProvider;
import com.example.fashionstore_system.payload.LoginRequest;
import com.example.fashionstore_system.payload.LoginResponse;
import com.example.fashionstore_system.repository.RoleRepository;
import com.example.fashionstore_system.repository.UserRepository;
import com.example.fashionstore_system.security.MyUserDetail;
import com.example.fashionstore_system.service.CustomerService;
import com.example.fashionstore_system.service.MailService;
import com.example.fashionstore_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@Controller
public class AppController {
    @Autowired
    private UserService userService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private MailService mailService;

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @GetMapping("/login")
    public String loginPage() {
        //prevent user return back to login page if they already login to the system
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }
        return "redirect:/";
    }
    @PostMapping("/login")
    public LoginResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        // Xác thực thông tin người dùng Request lên
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        // Nếu không xảy ra exception tức là thông tin hợp lệ
        // Set thông tin authentication vào Security Context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Trả về jwt cho người dùng.
        String jwt = tokenProvider.generateToken((MyUserDetail) authentication.getPrincipal());
        return new LoginResponse(jwt);
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
//    @GetMapping("/verify")
//    public String verifyAccount(@Param("code") String code, Model model) {
//        boolean verified = userService.verify(code);
//        String pageTitle = verified ? "Verification Succeeded!" : "Verification Failed";
//        model.addAttribute("pageTitle", pageTitle);
//        return (verified ? "/verify_success" : "/verify_failed");
//    }
    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute(name = "user") User user, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setUsername(user.getUsername());
        user.setRole(roleRepository.getById(1));
        Customer customer = user.getCustomer();
        customerService.saveCustomer(customer);
        user.setCustomer(customer);
        userService.saveUser(user);
        String siteUrl = Utility.getSiteURL(request);
        userService.sendVerificationEmail(user, siteUrl);

        return "login";
    }}
