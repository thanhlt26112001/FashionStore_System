package com.example.fashionstore_system.controller;

import com.example.fashionstore_system.config.Utility;
import com.example.fashionstore_system.entity.Customer;
import com.example.fashionstore_system.entity.Staff;
import com.example.fashionstore_system.entity.User;
import com.example.fashionstore_system.jwt.JwtAuthenticationFilter;
import com.example.fashionstore_system.jwt.JwtTokenProvider;
import com.example.fashionstore_system.payload.LoginRequest;
import com.example.fashionstore_system.payload.LoginResponse;
import com.example.fashionstore_system.repository.RoleRepository;
import com.example.fashionstore_system.repository.UserRepository;
import com.example.fashionstore_system.security.MyUserDetail;
import com.example.fashionstore_system.service.*;
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
    private StaffService staffService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private CartService cartService;

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

    @RequestMapping("/accountLocked")
    public RedirectView accountLocked(RedirectAttributes model) {
        model.addFlashAttribute("alert", "Your account has been locked!");
        return new RedirectView("/login");
    }

    @RequestMapping("/404")
    public String notFoundPage() {
        return "404";
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
    public RedirectView sendPassword(RedirectAttributes model, @RequestParam(name = "email") String email) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = new User();
        try {
            user = customerService.findByEmail(email).getUser();
        } catch (Exception e) {
            model.addFlashAttribute("alert", "Email not exist!!");
            return new RedirectView("/forgot_password");
        }
        String newPass = randomPassword();
        user.setPassword(encoder.encode(newPass));
        userService.saveUser(user);
        mailService.sendSimpleMessage(email, "Reset password", "This is your new password: " + newPass + "\n" +
                "Please login and change password!!");
        model.addFlashAttribute("alert", "Password changed!! Please check your email!!");
        return new RedirectView("/login");
    }

    private String randomPassword() {
        String character = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "abcdefghijklmnopqrstuvwxyz"
                + "0123456789"
                + "@$!%*#?&";
        Random random = new Random();
        String generatedString = "";
        for (int i = 0; i < 8; i++) {
            generatedString += character.charAt(random.nextInt(character.length()));
        }
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
    public String changePassword(Model model) {
        //prevent user return back to login page if they already login to the system
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }
        model.addAttribute("size_carts", cartService.getCartSize());
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
        return "edit_Customer_Profile";
    }

    //function save customer by id
    @PostMapping("/customeruser/save")
    public RedirectView saveCustomerUser(@ModelAttribute("user") User user,
                                         RedirectAttributes model) {
        Customer customerSave = new Customer();
        customerSave.setId(user.getCustomer().getId());
        customerSave.setEmail(user.getCustomer().getEmail());
        customerSave.setStatus(user.getCustomer().getStatus());
        customerSave.setPoint(user.getCustomer().getPoint());
        customerSave.setName(user.getCustomer().getName());
        customerSave.setPhone(user.getCustomer().getPhone());
        customerSave.setAddress(user.getCustomer().getAddress());
        customerSave.setAvatar(user.getCustomer().getAvatar());
        customerSave.setCreatedAt(user.getCustomer().getCreatedAt());
        customerSave.setBirthday(user.getCustomer().getBirthday());
        User userSave = new User();
        userSave.setId(user.getId());
        userSave.setUsername(user.getUsername());
        userSave.setPassword(user.getPassword());
        userSave.setRole(user.getRole());
        userSave.setCreatedAt(user.getCreatedAt());
        userSave.setCustomer(customerSave);
        if (user.getStaff().getId() != null) {
            userSave.setStaff(staffService.get(user.getStaff().getId()));
        }
        userService.saveUser(userSave);
        return new RedirectView("/product/listproducts");
    }


}


