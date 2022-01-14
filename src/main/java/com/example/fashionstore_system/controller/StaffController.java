package com.example.fashionstore_system.controller;

import com.example.fashionstore_system.entity.Customer;
import com.example.fashionstore_system.entity.Staff;
import com.example.fashionstore_system.entity.User;
import com.example.fashionstore_system.repository.CustomerRepository;
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
    private CustomerService customerService;

    @Autowired
    private UserService userService;

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

    // function saveStaff
    @RequestMapping(value = "/saveStaff", method = RequestMethod.POST)
    public String saveStaffCustomer(@ModelAttribute("user") User user) {
        user.setPassword(user.getPassword());
        user.setUsername(user.getUsername());
        user.setRole(roleRepository.getById(user.getRole().getId()));
        Customer customer = user.getCustomer();
        customer.setName(user.getStaff().getName());
        customer.setEmail(user.getStaff().getEmail());
        customer.setPhone(user.getStaff().getPhone());
        customer.setEmail(user.getCustomer().getEmail());
        customer.setBirthday(user.getCustomer().getBirthday());
        customer.setAvatar(user.getStaff().getAvatar());
        customer.setPoint(0);
        customerService.saveCustomer(customer);
        Staff staff = user.getStaff();
        staffService.saveStaff(staff);
        user.setStaff(staff);
        userService.saveUser(user);
        return "redirect:/staff";
    }
    //Function saveStaffEdit
    @RequestMapping(value = "/saveStaffEdit", method = RequestMethod.POST)
    public String saveStaffEdit(@ModelAttribute("user") User user) {
        //user
        User userSave = userService.getById(user.getId());
        userSave.setUsername(user.getUsername());
        userSave.setPassword(user.getPassword());
        userSave.setRole(roleRepository.getById(user.getRole().getId()));
        //customer
        Customer customerSave = customerService.getById(user.getCustomer().getId());
        customerSave.setName(user.getCustomer().getName());
        customerSave.setEmail(user.getCustomer().getEmail());
        customerSave.setPhone(user.getCustomer().getPhone());
        customerSave.setAddress(user.getCustomer().getAddress());
        customerSave.setBirthday(user.getCustomer().getBirthday());
        //Staff
        Staff staffSave = staffService.getById(user.getStaff().getId());
        staffSave.setAvatar(user.getStaff().getAvatar());
        staffSave.setEmail(user.getStaff().getEmail());
        staffSave.setPhone(user.getStaff().getPhone());
        staffSave.setName(user.getStaff().getName());
        staffSave.setUser(userSave);
        customerService.saveCustomer(customerSave);
        staffService.saveStaff(staffSave);
        userService.saveUser(userSave);
        return "redirect:/staff";
    }
    // function edit staff
    @RequestMapping("/editStaff/{id}")
    public ModelAndView showEditStaffPage(@PathVariable(name = "id") int id) {
        ModelAndView mav = new ModelAndView("edit_staff");
        User user = userService.getById(id);
        mav.addObject("user", user);
        return mav;
    }

    // function delete staff
    @RequestMapping("/deleteStaff/{id}")
    public String deleteStaff(@PathVariable(name = "id") int id) {
        User user = userService.getById(id);
       // int staffId = user.getStaff().getId();
        userService.deleteUser(id);
        // staffService.delete(staffId);
        return "redirect:/staff";
    }
}

