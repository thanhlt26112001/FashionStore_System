package com.example.fashionstore_system.controller;

import com.example.fashionstore_system.entity.Customer;
import com.example.fashionstore_system.entity.Product;
import com.example.fashionstore_system.entity.Staff;
import com.example.fashionstore_system.entity.User;
import com.example.fashionstore_system.repository.CustomerRepository;
import com.example.fashionstore_system.repository.RoleRepository;
import com.example.fashionstore_system.repository.StaffRepository;
import com.example.fashionstore_system.repository.UserRepository;
import com.example.fashionstore_system.service.CustomerService;
import com.example.fashionstore_system.service.ProductService;
import com.example.fashionstore_system.service.StaffService;
import com.example.fashionstore_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/admin")
@Controller
public class AdminController {

    //dungnv made this
    // Admin Staff Manager
    @Autowired
    private StaffService staffService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private CustomerRepository customerRepository;
    //anhht made this
    //product management
    //show list product

    @Autowired
    private ProductService productService;

    @RequestMapping("/product")
    public String viewProductAdmin(Model model, @Param("keyword") String keyword) {
        List<Product> listProducts = productService.listAll(keyword);
        model.addAttribute("listProducts", listProducts);
        model.addAttribute("keyword", keyword);
        return "listProductAdmin";
    }

    @RequestMapping("/product/new")
    public String showNewProductPage(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "createProductAdmin";
    }

    @RequestMapping(value = "/product/save", method = RequestMethod.POST)
    public String saveProduct(@ModelAttribute("product") Product product) {
        productService.saveProduct(product);
        return "redirect:/";
    }

    @RequestMapping("/product/edit/{id}")
    public ModelAndView showEditProductPage(@PathVariable(name = "id") int id) {
        ModelAndView mav = new ModelAndView("editProductAdmin");
        Product product = productService.getProduct(id);
        mav.addObject("product", product);
        return mav;
    }

    @RequestMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable(name = "id") int id) {
        productService.deleteProduct(id);
        return "redirect:/";
    }

    //Admin Staff Manager
    // functions show list Staff
    @RequestMapping("/staff")
    public String viewStaffAdmin(Model model, @Param("keyword") String keyword) {
        List<Staff> listStaffs = staffService.listAll(keyword);
        model.addAttribute("listStaffs", listStaffs);
        model.addAttribute("keyword", keyword);
        return "listStaffAdmin";
    }

    // function create new Staff
    @RequestMapping("/newStaff")
    public String showNewStaffForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "new_staff";
    }

    // function saveStaff
    @RequestMapping(value = "/saveStaff", method = RequestMethod.POST)
    public RedirectView saveStaffCustomer(@Valid User user, RedirectAttributes model) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
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
        if (userRepository.findByUsername(user.getUsername()) != null) {
            model.addFlashAttribute("alert_Username", "Username is exited!");
            return new RedirectView("/admin/newStaff");
        } else if (staffRepository.findByEmail(user.getStaff().getEmail()) != null) {
            model.addFlashAttribute("alert_Email", "Email is exited!");
            return new RedirectView("/admin/newStaff");
        }
        customerService.saveCustomer(customer);
        Staff staff = user.getStaff();
        staffService.saveStaff(staff);
        user.setStaff(staff);
        userService.saveUser(user);
        model.addAttribute("user", user);
        return new RedirectView("/admin/staff");
    }
    //Function saveStaffEdit
    @RequestMapping(value = "/saveStaffEdit", method = RequestMethod.POST)
    public RedirectView saveStaffEdit(@ModelAttribute("user") User user, RedirectAttributes model) {
        //user
        User userSave = userService.getById(user.getId());
        userSave.setUsername(user.getUsername());
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
        if (userRepository.findByUsername(user.getUsername()) != null) {
            model.addFlashAttribute("alert_Username", "Username is exited!");
            return new RedirectView("/admin/editStaff");
        } else if (staffRepository.findByEmail(user.getStaff().getEmail()) != null) {
            model.addFlashAttribute("alert_Email", "Email is exited!");
            return new RedirectView("/admin/editStaff");
        }
        customerService.saveCustomer(customerSave);
        staffService.saveStaff(staffSave);
        userService.saveUser(userSave);
        return  new RedirectView("/admin/staff");
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
        return "redirect:/admin/staff";
    }
}
