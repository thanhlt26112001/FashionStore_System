package com.example.fashionstore_system.controller;

import com.example.fashionstore_system.entity.Customer;
import com.example.fashionstore_system.entity.Staff;
import com.example.fashionstore_system.entity.User;
import com.example.fashionstore_system.repository.RoleRepository;
import com.example.fashionstore_system.service.CustomerService;
import com.example.fashionstore_system.service.StaffService;
import com.example.fashionstore_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@Controller
public class StaffController {

    @Autowired
    private StaffService staffService;

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private RoleRepository roleRepository;

    // functions show list Staff
    @RequestMapping("/staff")
    public String viewHomepage(Model model, @Param("keyword") String keyword) {
        List<Staff> listStaffs = staffService.listAll(keyword);
        model.addAttribute("listStaffs", listStaffs);
        model.addAttribute("keyword", keyword);
        return "index";
    }

    // function create new Staff
    @RequestMapping("/new")
    public String showNewStaffForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "new_staff";
    }

    // function save
    @RequestMapping(value = "/saveStaff", method = RequestMethod.POST)
    public String saveStaff(@ModelAttribute("user") User user) {
        user.setPassword(user.getPassword());
        user.setUsername(user.getUsername());
        user.setRole(roleRepository.getById(2));
        Staff staff = user.getStaff();
        staffService.save(staff);
        user.setStaff(staff);
        userService.saveUser(user);
        return "redirect:/staff";
    }
    //
    @RequestMapping(value = "/saveStaffEdit", method = RequestMethod.POST)
    public String saveStaffEdit(@ModelAttribute("user") User user) {
        user.setId(user.getId());
        user.setPassword(user.getPassword());
        user.setUsername(user.getUsername());
        user.setRole(roleRepository.getById(2));
        Staff staff = user.getStaff();
        staff.setId(staff.getId());
        staffService.save(staff);
        user.setStaff(staff);
        userService.saveUser(user);
        return "redirect:/staff";

    }
    // function edit staff
    @RequestMapping("/edit/{id}")
    public ModelAndView showEditStaffPage(@PathVariable(name = "id") int id) {
        ModelAndView mav = new ModelAndView("edit_staff");
        User user = userService.getById(id);
        mav.addObject("user", user);
        return mav;
    }

    // function delete staff
    @RequestMapping("/delete/{id}")
    public String deleteStaff(@PathVariable(name = "id") int id) {
        User user = userService.getById(id);
       int staffId = user.getStaff().getId();
        userService.deleteUser(id);

       // staffService.delete(staffId);
        return "redirect:/staff";
    }

}

