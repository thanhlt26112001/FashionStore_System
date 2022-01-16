package com.example.fashionstore_system.controller;

import com.example.fashionstore_system.entity.Customer;
import com.example.fashionstore_system.entity.Product;
import com.example.fashionstore_system.entity.ProductImage;
import com.example.fashionstore_system.entity.User;
import com.example.fashionstore_system.repository.ProductImageRepository;
import com.example.fashionstore_system.repository.RoleRepository;
import com.example.fashionstore_system.service.CustomerService;
import com.example.fashionstore_system.service.ProductService;
import com.example.fashionstore_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RequestMapping("/admin")
@Controller
public class AdminController {
    //anhht made this
    //product management
    //show list product
    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private RoleRepository roleRepository;

    //function show list product
    @RequestMapping("/product")

    public String listProductAdmin(Model model,
                                   @RequestParam(value = "keyword", defaultValue = "") String keyword) {
        return listProductAdminByPage(1, keyword, model);
    }

    //function show list product by page
    @RequestMapping("/product/{Pagenumber}")
    public String listProductAdminByPage(@PathVariable(name = "pageNumber") int currentPage,
                                         @Param("keyword") String keyword,
                                         Model model) {
        Page<Product> listProducts = productService.listAll(currentPage, keyword);
        model.addAttribute("listProducts", listProducts);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("keyword", keyword);
        return "listProductAdmin";
    }

    //function create new product
    @RequestMapping("/product/new")
    public String showNewProductPage(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        model.addAttribute("listCategory",productService.getCategoryList());
        return "createProductAdmin";
    }

    //function save product
    @RequestMapping(value = "/product/save", method = RequestMethod.POST)
    public String saveProduct(@ModelAttribute("product") Product product) {
        productService.saveProduct(product);
        return "redirect:/admin/product";
    }

    //function save image product
    @RequestMapping(value = "/product/saveImage/{id}", method = RequestMethod.POST)
    public String saveImageProduct(@ModelAttribute("productImage") ProductImage productImage,
                                   @PathVariable (name="id") int productId){
        productImage.setProduct(productService.getProduct(productId));
        productImage.setId(null);
        productService.saveImageProduct(productImage);
        return "redirect:/admin/product/edit/"+productId;
    }

    //function delete image product by id
    @RequestMapping("/product/deleteImage/{id}")
    public String deleteImageProduct(@PathVariable(name = "id") int id) {
        int productId = productService.findImageById(id).getProduct().getId();
        productService.deleteImageProduct(id);
        return "redirect:/admin/product/edit/"+productId;
    }

    //function edit product by id
    @RequestMapping("/product/edit/{id}")
    public ModelAndView showEditProductPage(@PathVariable(name = "id") int id) {
        ModelAndView mav = new ModelAndView("editProductAdmin");
        mav.addObject("listCategory",productService.getCategoryList());
        Product product = productService.getProduct(id);
        mav.addObject("product", product);
        ProductImage productImage = new ProductImage();
        mav.addObject("productImage",productImage);
        List<ProductImage> productImageList = productService.findImageProductById(id);
        mav.addObject("productImageList", productImageList);
        return mav;
    }

    //function delete product by id
    @RequestMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable(name = "id") int id) {
        productService.deleteProduct(id);
        return "redirect:/admin/product";
    }

    //function show list customer
    @RequestMapping("/customer")
    public String viewCustomerAdmin(Model model, @Param("keyword") String keyword) {
        List<Customer> listCustomers = customerService.listAll(keyword);
        model.addAttribute("listCustomers", listCustomers);
        model.addAttribute("keyword", keyword);
        return "listCustomerAdmin";
    }

    //function create new customer
    @RequestMapping("/customer/new")
    public String showNewCustomerPage(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "createCustomerAdmin";
    }

    //function save customer
    @RequestMapping(value = "/customer/save", method = RequestMethod.POST)
    public String saveCustomerAdmin(@ModelAttribute("user") User user) {
        user.setPassword(user.getPassword());
        user.setUsername(user.getUsername());
        user.setRole(roleRepository.getById(1));
        Customer customer = user.getCustomer();
        customer.setName(user.getCustomer().getName());
        customer.setEmail(user.getCustomer().getEmail());
        customer.setPhone(user.getCustomer().getPhone());
        customer.setAddress(user.getCustomer().getAddress());
        customer.setBirthday(user.getCustomer().getBirthday());
        customer.setAvatar(user.getCustomer().getAvatar());
        customerService.saveCustomer(customer);
        user.setCustomer(customer);
        userService.saveUser(user);
        return "redirect:/admin/customer";
    }

    //function save customerEdit
    @RequestMapping(value = "/customer/saveEdit", method = RequestMethod.POST)
    public String saveCustomerEdit(@ModelAttribute("user") User user) {
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
        customerSave.setAvatar(user.getCustomer().getAvatar());
        customerService.saveCustomer(customerSave);
        userService.saveUser(userSave);
        return "redirect:/admin/customer";
    }

    //function edit customer by id
    @RequestMapping("/customer/edit/{id}")
    public ModelAndView showEditCustomerPage(@PathVariable(name = "id") int id) {
        ModelAndView mav = new ModelAndView("editCustomerAdmin");
        User user = userService.getById(id);
        mav.addObject("user", user);
        return mav;
    }

    //function delete customer by id
    @RequestMapping("/customer/delete/{id}")
    public String deleteCustomer(@PathVariable(name = "id") int id) {
        User user = userService.getById(id);
        userService.deleteUser(id);
        return "redirect:/admin/customer";
    }
}
