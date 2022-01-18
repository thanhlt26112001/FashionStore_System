package com.example.fashionstore_system.controller;

import com.example.fashionstore_system.config.Utility;
import com.example.fashionstore_system.entity.Category;
import com.example.fashionstore_system.entity.Customer;
import com.example.fashionstore_system.entity.User;
import com.example.fashionstore_system.jwt.JwtAuthenticationFilter;
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
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.Random;

@Controller
public class UserController {
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
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @GetMapping("/login")
    public String loginPage() {
        //prevent user return back to login page if they already login to the system
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }
        return "redirect:/";
    }

    @RequestMapping("/loginFail")
    public RedirectView loginFail(RedirectAttributes model) {
        model.addFlashAttribute("alert", "Wrong Username or Password!");
        return new RedirectView("/login");
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            User user = new User();
            model.addAttribute("user", user);
            return "sign_up";
        }
        return "redirect:/";
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
    public String sendPassword(Model model, @RequestParam(name = "email") String email) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = customerService.findByEmail(email).getUser();
        String newPass = randomPassword();
        user.setPassword(encoder.encode(newPass));
        userService.saveUser(user);
        mailService.sendSimpleMessage(email, "Reset password", "This is your new password: " + newPass + "\n" +
                "Please login and change password!!");
        model.addAttribute("alert", "Email sent!!");
        return "redirect:/login";
    }

    private String randomPassword() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 8;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute(name = "user") User user,
                           HttpServletRequest request, Model model)
            throws MessagingException, UnsupportedEncodingException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String encodedPassword = encoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            user.setUsername(user.getUsername());
            user.setRole(roleRepository.getById(1));
            Customer customer = user.getCustomer();
            if (customerService.findByEmail(user.getCustomer().getEmail()) == null
                    && userService.findByUsername(user.getUsername()) == null) {
                customerService.saveCustomer(customer);
                user.setCustomer(customer);
                userService.saveUser(user);
            } else {
                model.addAttribute("alert", "Register failed!!!");
                return "redirect:/login";
            }
            String siteUrl = Utility.getSiteURL(request);
            userService.sendVerificationEmail(user, siteUrl);
            model.addAttribute("alert", "Register success!!!");
            return "redirect:/login";
        }
        return "redirect:/";
    }

    @GetMapping("/changePassword")
    public String changePassword() {
        //prevent user return back to login page if they already login to the system
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }
        return "changePassword";
    }

    @PostMapping("/saveChangePassword")
    public RedirectView saveChangePassword(HttpServletRequest request, HttpServletResponse response, RedirectAttributes model) {
        //prevent user return back to login page if they already login to the system
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return new RedirectView("/login");
        }
        User user = userService.findByUsername(authentication.getName());
        String curPass = request.getParameter("currentpass");
        String newPass = request.getParameter("newpass");
        String reNewPass = request.getParameter("re_newpass");
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(curPass, user.getPassword())) {
            model.addFlashAttribute("alert_currentPass", "Wrong password!");
            return new RedirectView("/changePassword");
        } else if (curPass.equals(newPass) || newPass.length() < 6) {
            model.addFlashAttribute("alert_newPass", "New password must different and has more than 6 characters!");
            return new RedirectView("/changePassword");
        } else if (!newPass.equals(reNewPass)) {
            model.addFlashAttribute("alert_reNewPass", "Enter exactly new password again!");
            return new RedirectView("/changePassword");
        }
        user.setPassword(encoder.encode(newPass));
        userService.saveUser(user);
        model.addFlashAttribute("alert", "Change password successfully!");
        mailService.sendSimpleMessage(user.getCustomer().getEmail(), "Reset password", "Your password has been changed successfully!!");
        new SecurityContextLogoutHandler().logout(request, response, authentication);
        return new RedirectView("/login");
    }

    //function edit customer by id
    @RequestMapping("/customeruser/edit/")
    public String showEditCustomerUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }
        User user = userService.findByUsername(authentication.getName());
        model.addAttribute("user", user);
        return "edit_CustomerUser_Admin";
    }
    //function save customer by id
    @PostMapping("/customeruser/save")
    public RedirectView saveCustomerUser(@ModelAttribute("customer") Customer customer,
                                        RedirectAttributes model) {
        customerService.saveCustomer(customer);
        return new RedirectView("/product/listproducts");
    }


}


