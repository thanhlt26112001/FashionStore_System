package com.example.fashionstore_system.controller;

import com.example.fashionstore_system.entity.Staff;
import com.example.fashionstore_system.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private StaffService service;

    // functions show list Staff
    @RequestMapping("/staff")
    public String viewHomepage(Model model, @Param("keyword") String keyword) {
        List<Staff> listStaffs = service.listAll(keyword);


//        for (Staff staff: listStaffs) {
//            if (staff.getUser().getRoleid)
//        }
        model.addAttribute("listStaffs", listStaffs);
        model.addAttribute("keyword", keyword);
        return "index";
    }

    // function create new Staff
    @RequestMapping("/new")
    public String showNewStaffForm(Model model) {
        Staff staff = new Staff();
        model.addAttribute("staff", staff);

        return "new_staff";

    }

    // function save
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveProduct(@ModelAttribute("staff") Staff staff) {
        service.save(staff);

        return "redirect:/staff";
    }

    // function edit staff
    @RequestMapping("/edit/{id}")
    public ModelAndView showEditStaffPage(@PathVariable(name = "id") int id) {
        ModelAndView mav = new ModelAndView("edit_staff");
        Staff staff = service.get(id);
        mav.addObject("staff", staff);

        return mav;
    }

    // function delete staff
    @RequestMapping("/delete/{id}")
    public String deleteStaff(@PathVariable(name = "id") int id) {
        service.delete(id);
        return "redirect:/staff";
    }

}

