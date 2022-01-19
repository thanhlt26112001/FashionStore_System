package com.example.fashionstore_system.controller;

import com.example.fashionstore_system.entity.*;
import com.example.fashionstore_system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.example.fashionstore_system.entity.Customer;
import com.example.fashionstore_system.entity.Product;
import com.example.fashionstore_system.entity.ProductImage;
import com.example.fashionstore_system.entity.User;
import com.example.fashionstore_system.repository.RoleRepository;
import com.example.fashionstore_system.entity.Staff;
import com.example.fashionstore_system.repository.StaffRepository;
import com.example.fashionstore_system.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/admin")
@Controller
public class AdminController {

    @Autowired
    private PromotionService promotionService;
    @Autowired
    private CategoriesService categoriesService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private  ShippingUnitService shippingUnitService;
    //anhht made this
    //product management
    //show list product
    @Autowired
    private ProductService productService;

    //function show list product
    @RequestMapping("/product")
    public String listProductAdmin(Model model,
                                   @RequestParam(value = "keyword", defaultValue = "") String keyword,
                                   @RequestParam(value = "sortField", defaultValue = "id") String sortField,
                                   @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir) {
        return listProductAdminByPage(1, sortField, sortDir, keyword, model);
    }

    //function show list product by page
    @RequestMapping("/product/{pageNumber}")
    public String listProductAdminByPage(@PathVariable(name = "pageNumber") int currentPage,
                                         @RequestParam("sortField") String sortField,
                                         @RequestParam("sortDir") String sortDir,
                                         @RequestParam("keyword") String keyword,
                                         Model model) {
        Page<Product> page = productService.listAllProduct(currentPage, sortField, sortDir, keyword);
        long totalItems = page.getTotalElements();
        int totalPages = page.getTotalPages();
        List<Product> listproduct = page.getContent();
        List<Product> listproductRegister = new ArrayList<>();
        model.addAttribute("listproductRegister", listproductRegister);
        model.addAttribute("listproduct", listproduct);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("keyword", keyword);
        model.addAttribute("query", "?sortField=" + sortField + "&sortDir="
                + sortDir + "&keyword=" + keyword);
        return "listProductAdmin";
    }

    //function create new product
    @RequestMapping("/product/new")
    public String showNewProductPage(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        model.addAttribute("listCategory", productService.getCategoryList());
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
                                   @PathVariable(name = "id") int productId) {
        productImage.setProduct(productService.getProduct(productId));
        productImage.setId(null);
        productService.saveImageProduct(productImage);
        return "redirect:/admin/product/edit/" + productId;
    }

    //function delete image product by id
    @RequestMapping("/product/deleteImage/{id}")
    public String deleteImageProduct(@PathVariable(name = "id") int id) {
        int productId = productService.findImageById(id).getProduct().getId();
        productService.deleteImageProduct(id);
        return "redirect:/admin/product/edit/" + productId;
    }

    //function edit product by id
    @RequestMapping("/product/edit/{id}")
    public ModelAndView showEditProductPage(@PathVariable(name = "id") int id) {
        ModelAndView mav = new ModelAndView("editProductAdmin");
        mav.addObject("listCategory", productService.getCategoryList());
        Product product = productService.getProduct(id);
        mav.addObject("product", product);
        ProductImage productImage = new ProductImage();
        mav.addObject("productImage", productImage);
        List<ProductImage> productImageList = productService.findImageProductById(id);
        mav.addObject("productImageList", productImageList);
        return mav;
    }

    //function delete product by id
    @RequestMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable(name = "id") int id) {
        Product product = productService.getProduct(id);
        product.setStatus(0);
        productService.saveProduct(product);
        return "redirect:/admin/product";
    }

    //function show customer list
    @GetMapping("/customer")
    public String viewCustomer(Model model,
                               @RequestParam(value = "keyword", defaultValue = "") String keyword,
                               @RequestParam(value = "sortField", defaultValue = "id") String sortField,
                               @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir) {
        return listByPagesCustomer(1, sortField, sortDir, keyword, model);
    }

    @GetMapping("/customer/{pageNumber}")
    public String listByPagesCustomer(@PathVariable(name = "pageNumber") int currentPage,
                                      @RequestParam(value = "sortField", defaultValue = "id") String sortField,
                                      @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir,
                                      @RequestParam(value = "keyword", defaultValue = "") String keyword,
                                      Model model) {
        Page<Customer> page = customerService.listAllCustomer(currentPage, sortField, sortDir, keyword);
        long totalItems = page.getTotalElements();
        int totalPages = page.getTotalPages();
        List<Customer> listcustomer = page.getContent();
        model.addAttribute("listcustomer", listcustomer);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("keyword", keyword);
        model.addAttribute("query", "?sortField=" + sortField + "&sortDir="
                + sortDir + "&keyword=" + keyword);
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
    public RedirectView saveCustomerAdmin(@Valid User user, RedirectAttributes model) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setUsername(user.getUsername());
        user.setRole(roleRepository.getById(1));
        Customer customer = user.getCustomer();
        customer.setName(user.getCustomer().getName());
        customer.setEmail(user.getCustomer().getEmail());
        customer.setPhone(user.getCustomer().getPhone());
        customer.setAddress(user.getCustomer().getAddress());
        customer.setBirthday(user.getCustomer().getBirthday());
        customer.setAvatar(user.getCustomer().getAvatar());
        customer.setPoint(0);
        if (userRepository.findByUsername(user.getUsername()) != null) {
            model.addFlashAttribute("alert_Username", "Username is exited!");
            return new RedirectView("/admin/customer/new");
        } else if (customerService.findByEmail(user.getCustomer().getEmail()) != null) {
            model.addFlashAttribute("alert_Email", "Email is exited!");
            return new RedirectView("/admin/customer/new");
        }
        customerService.saveCustomer(customer);
        user.setCustomer(customer);
        userService.saveUser(user);
        return new RedirectView("/admin/customer");
    }


    //function save customerEdit
    @RequestMapping(value = "/customer/saveEdit", method = RequestMethod.POST)
    public String saveCustomerEdit(@ModelAttribute("user") User user) {
        //user
        User userSave = userService.getById(user.getId());
        userSave.setUsername(user.getUsername());
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

    //Admin Staff Manager
    // functions show list Staff
//    @RequestMapping("/staff")
//    public String viewStaffAdmin(Model model, @Param("keyword") String keyword) {
//        List<Staff> listStaffs = staffService.listAll(keyword);
//        model.addAttribute("listStaffs", listStaffs);
//        model.addAttribute("keyword", keyword);
//        return "listStaffAdmin";
//    }
    @GetMapping("/staff")
    public String viewStaff(Model model,
                            @RequestParam(value = "keyword", defaultValue = "") String keyword,
                            @RequestParam(value = "sortField", defaultValue = "id") String sortField,
                            @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir) {
        return listByPagesStaff(1, sortField, sortDir, keyword, model);
    }
    @GetMapping("/staff/{pageNumber}")
    public String listByPagesStaff(@PathVariable(name = "pageNumber") int currentPage,
                                   @RequestParam(value = "sortField", defaultValue = "id") String sortField,
                                   @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir,
                                   @RequestParam(value = "keyword", defaultValue = "") String keyword,
                                   Model model) {
        Page<Staff> page = staffService.listAllStaff(currentPage, sortField, sortDir, keyword);
        long totalItems = page.getTotalElements();
        int totalPages = page.getTotalPages();
        List<Staff> StaffList = page.getContent();
        model.addAttribute("StaffList", StaffList);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("keyword", keyword);
        model.addAttribute("query", "?sortField=" + sortField + "&sortDir="
                + sortDir + "&keyword=" + keyword);
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
//        customerSave.setName(user.getCustomer().getName());
//        customerSave.setEmail(user.getCustomer().getEmail());
//        customerSave.setPhone(user.getCustomer().getPhone());
//        customerSave.setAddress(user.getCustomer().getAddress());
//        customerSave.setBirthday(user.getCustomer().getBirthday());
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
        return new RedirectView("/admin/staff");
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
        int staffId = user.getStaff().getId();
        user.setStaff(null);
        user.setRole(roleRepository.getById(1));
        // int staffId = user.getStaff().getId();
        userService.saveUser(user);
        staffService.delete(staffId);
        // staffService.delete(staffId);
        return "redirect:/admin/staff";
    }

    //Promotions
    @GetMapping("/listPromotions")
    public String viewCourse(Model model,
                             @RequestParam(value = "keyword", defaultValue = "") String keyword,
                             @RequestParam(value = "sortField", defaultValue = "id") String sortField,
                             @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir) {
        return listByPages(1, sortField, sortDir, keyword, model);
    }

    @GetMapping("/listPromotions/add")
    public String addPromotion(Model model) {
        Promotion promotion = new Promotion();
        model.addAttribute("PROMOTION", promotion);
        model.addAttribute("addpromotions", "/admin/listPromotions/saveOrUpdate");
        return "add_promotion_Admin";
    }

    @PostMapping("/listPromotions/saveOrUpdate")
    public RedirectView saveAdd(@ModelAttribute("PROMOTION") Promotion promotion,
                                @RequestParam(name = "Monday", required = false) String Monday,
                                @RequestParam(name = "Tuesday", required = false) String Tuesday,
                                @RequestParam(name = "Wednesday", required = false) String Wednesday,
                                @RequestParam(name = "Thursday", required = false) String Thursday,
                                @RequestParam(name = "Friday", required = false) String Friday,
                                @RequestParam(name = "Saturday", required = false) String Saturday,
                                @RequestParam(name = "Sunday", required = false) String Sunday,
                                @RequestParam(name = "AllWeek", required = false) String AllWeek,
                                RedirectAttributes model) {
        Promotion promotion1 = promotionService.findByName(promotion.getName());
        if (promotion1 != null) {
            model.addFlashAttribute("alert", "Promotion name is existed!!!");
            return new RedirectView("/admin/listPromotions/add");
        }
        if (promotion.getDiscount() > 100 || promotion.getDiscount() < 0) {
            model.addFlashAttribute("alert", "Discount must from 0 to 100!!!");
            return new RedirectView("/admin/listPromotions/add");
        }
        if (promotion.getStartDate().after(promotion.getEndDate())) {
            model.addFlashAttribute("alert", "Start Date must before End Date!!!");
            return new RedirectView("/admin/listPromotions/add");
        }
        String applyDay = "";
        if (Monday != null && Tuesday != null && Wednesday != null && Thursday != null &&
                Friday != null && Saturday != null && Sunday != null || AllWeek != null) {
            promotion.setApplyDay("AllWeek");
            promotionService.save(promotion);
            return new RedirectView("/admin/listPromotions");
        }
        if (Monday != null) {
            applyDay = applyDay + Monday + " ";
        }
        if (Tuesday != null) {
            applyDay = applyDay + Tuesday + " ";
        }
        if (Wednesday != null) {
            applyDay = applyDay + Wednesday + " ";
        }
        if (Thursday != null) {
            applyDay = applyDay + Thursday + " ";
        }
        if (Friday != null) {
            applyDay = applyDay + Friday + " ";
        }
        if (Saturday != null) {
            applyDay = applyDay + Saturday + " ";
        }
        if (Sunday != null) {
            applyDay = applyDay + Sunday + " ";
        }
        promotion.setApplyDay(applyDay);
        promotionService.save(promotion);
        return new RedirectView("/admin/listPromotions");
    }

    @RequestMapping("/edit/{id}")
    public ModelAndView showEditPage(@PathVariable(name = "id") int id) {
        ModelAndView mav = new ModelAndView("edit_promotion_Admin");
        Promotion promotion = promotionService.getById(id);
        String applyDay = promotion.getApplyDay();
        if (applyDay.equals("AllWeek")) {
            mav.addObject("AllWeek", true);
        }
        if (applyDay.contains("Tuesday")) {
            mav.addObject("Tuesday", true);
        }
        if (applyDay.contains("Wednesday")) {
            mav.addObject("Wednesday", true);
        }
        if (applyDay.contains("Thursday")) {
            mav.addObject("Thursday", true);
        }
        if (applyDay.contains("Friday")) {
            mav.addObject("Friday", true);
        }
        if (applyDay.contains("Saturday")) {
            mav.addObject("Saturday", true);
        }
        if (applyDay.contains("Sunday")) {
            mav.addObject("Sunday", true);
        }
        mav.addObject("promotion", promotion);
        return mav;
    }

    @PostMapping("/listPromotions/editAndSave")
    public RedirectView saveEdit(@ModelAttribute("PROMOTION") Promotion promotion,
                                 @RequestParam(name = "Monday", required = false) String Monday,
                                 @RequestParam(name = "Tuesday", required = false) String Tuesday,
                                 @RequestParam(name = "Wednesday", required = false) String Wednesday,
                                 @RequestParam(name = "Thursday", required = false) String Thursday,
                                 @RequestParam(name = "Friday", required = false) String Friday,
                                 @RequestParam(name = "Saturday", required = false) String Saturday,
                                 @RequestParam(name = "Sunday", required = false) String Sunday,
                                 @RequestParam(name = "AllWeek", required = false) String AllWeek,
                                 RedirectAttributes model) {
        if (promotion.getDiscount() > 100 || promotion.getDiscount() < 0) {
            model.addFlashAttribute("alert", "Discount must from 0 to 100!!!");
            return new RedirectView("/admin/edit/" + promotion.getId());
        }
        if (promotion.getStartDate().after(promotion.getEndDate())) {
            model.addFlashAttribute("alert", "Start Date must before End Date!!!");
            return new RedirectView("/admin/edit/" + promotion.getId());
        }
        String applyDay = "";
        if (Monday != null && Tuesday != null && Wednesday != null && Thursday != null &&
                Friday != null && Saturday != null && Sunday != null || AllWeek != null) {
            promotion.setApplyDay("AllWeek");
            promotionService.save(promotion);
            return new RedirectView("/admin/listPromotions");
        }
        if (Monday != null) {
            applyDay = applyDay + Monday + " ";
        }
        if (Tuesday != null) {
            applyDay = applyDay + Tuesday + " ";
        }
        if (Wednesday != null) {
            applyDay = applyDay + Wednesday + " ";
        }
        if (Thursday != null) {
            applyDay = applyDay + Thursday + " ";
        }
        if (Friday != null) {
            applyDay = applyDay + Friday + " ";
        }
        if (Saturday != null) {
            applyDay = applyDay + Saturday + " ";
        }
        if (Sunday != null) {
            applyDay = applyDay + Sunday + " ";
        }
        promotion.setApplyDay(applyDay);
        promotionService.save(promotion);
        return new RedirectView("/admin/listPromotions");
    }

    @RequestMapping("/delete/{id}")
    public String deletePromotion(@PathVariable(name = "id") int id) {
        promotionService.delete(id);
        return "redirect:/admin/listPromotions";
    }

    @GetMapping("/listPromotions/{pageNumber}")
    public String listByPages(@PathVariable(name = "pageNumber") int currentPage,
                              @RequestParam(value = "sortField", defaultValue = "id") String sortField,
                              @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir,
                              @RequestParam(value = "keyword", defaultValue = "") String keyword,
                              Model model) {
        Page<Promotion> page = promotionService.listAllPromotion(currentPage, sortField, sortDir, keyword);
        long totalItems = page.getTotalElements();
        int totalPages = page.getTotalPages();
        List<Promotion> listpromotions = page.getContent();
        model.addAttribute("listpromotions", listpromotions);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("keyword", keyword);
        model.addAttribute("query", "?sortField=" + sortField + "&sortDir="
                + sortDir + "&keyword=" + keyword);
        return "list_promotions_Admin";
    }

    //Promotions

    //Category
    @GetMapping("/category/listCategory")
    public String viewCategory(Model model,
                               @RequestParam(value = "keyword", defaultValue = "") String keyword,
                               @RequestParam(value = "sortField", defaultValue = "id") String sortField,
                               @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir) {
        return listByPagesCategory(1, sortField, sortDir, keyword, model);
    }

    @GetMapping("/category/listCategory/add")
    public String addCategory(Model model) {
        Category category = new Category();
        model.addAttribute("category", category);
        model.addAttribute("addcategory", "/admin/category/listCategory/save");
        return "add_category_Admin";
    }

    @PostMapping("/category/listCategory/save")
    public RedirectView saveAddCategory(@ModelAttribute("category") Category category,
                                        RedirectAttributes model) {
        Category category1 = categoriesService.findByName(category.getName());
        if (category1 != null) {
            model.addFlashAttribute("alert", "Category name is existed!!!");
            return new RedirectView("/admin/category/listCategory/add");
        }

        categoriesService.save(category);
        return new RedirectView("/admin/category/listCategory");
    }

    @RequestMapping("/category/edit/{id}")
    public ModelAndView showEditCategory(@PathVariable(name = "id") int id) {
        ModelAndView mav = new ModelAndView("edit_category_Admin");
        Category category = categoriesService.getById(id);
        mav.addObject("category", category);
        return mav;
    }

    @PostMapping("/category/listCategory/editAndSave")
    public RedirectView saveEditCategory(@ModelAttribute("category") Category category,
                                         RedirectAttributes model) {

        categoriesService.save(category);
        return new RedirectView("/admin/category/listCategory");
    }

    @RequestMapping("/category/delete/{id}")
    public String deleteCategory(@PathVariable(name = "id") int id) {
        categoriesService.delete(id);
        return "redirect:/admin/category/listCategory";
    }

    @GetMapping("/category/listCategory/{pageNumber}")
    public String listByPagesCategory(@PathVariable(name = "pageNumber") int currentPage,
                                      @RequestParam(value = "sortField", defaultValue = "id") String sortField,
                                      @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir,
                                      @RequestParam(value = "keyword", defaultValue = "") String keyword,
                                      Model model) {
        Page<Category> page = categoriesService.listAllCategory(currentPage, sortField, sortDir, keyword);
        long totalItems = page.getTotalElements();
        int totalPages = page.getTotalPages();
        List<Category> listcategory = page.getContent();
        model.addAttribute("listcategory", listcategory);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("keyword", keyword);
        model.addAttribute("query", "?sortField=" + sortField + "&sortDir="
                + sortDir + "&keyword=" + keyword);
        return "list_category_Admin";
    }
    // admin Order
    @GetMapping("/order")
    public String viewOrder(Model model,
                               @RequestParam(value = "keyword", defaultValue = "") String keyword,
                               @RequestParam(value = "sortField", defaultValue = "id") String sortField,
                               @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir) {
        return listByPagesOrder(1, sortField, sortDir, keyword, model);
    }
    @GetMapping("/order/{pageNumber}")
    public String listByPagesOrder(@PathVariable(name = "pageNumber") int currentPage,
                                      @RequestParam(value = "sortField", defaultValue = "id") String sortField,
                                      @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir,
                                      @RequestParam(value = "keyword", defaultValue = "") String keyword,
                                      Model model) {
        Page<Order> page = orderService.listAllOrder(currentPage, sortField, sortDir, keyword);
        long totalItems = page.getTotalElements();
        int totalPages = page.getTotalPages();
        List<Order> orderList = page.getContent();
        model.addAttribute("orderList", orderList);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("keyword", keyword);
        model.addAttribute("query", "?sortField=" + sortField + "&sortDir="
                + sortDir + "&keyword=" + keyword);
        return "listOrderAdmin";
    }

    //function OrderDetail
    @RequestMapping("/orderDetail/{id}")
    public ModelAndView showOrderDetailPage(@PathVariable(name = "id") int id) {
        ModelAndView mav = new ModelAndView("orderDetailAdmin");
        List<OrderDetail> orderDetailList = orderService.getOrderDetailByID(id);
        Order order = orderService.getById(id);
        mav.addObject("orderDetailList", orderDetailList);
        mav.addObject("order", order);
        return mav;
    }
    @RequestMapping(value = "/saveOrderEdit", method = RequestMethod.POST)
    public String saveOrderEdit(@ModelAttribute("order") Order order) {
        Order orderSave = orderService.getById(order.getId());
        orderSave.setPaymentStatus(order.getPaymentStatus());
        orderSave.setStatus(order.getStatus());
        orderService.saveOrder(orderSave);
        return "redirect:/admin/order" ;
    }
    // Admin revenua
    @RequestMapping("/revenue")
    public String viewHomeRevenua(Model model,@RequestParam(name = "year",defaultValue = "2022") int year) {

        model.addAttribute("year", year);
        model.addAttribute("january", orderService.getDataRevenueByMonthOfYear(1,year));
        model.addAttribute("February", orderService.getDataRevenueByMonthOfYear(2,year));
        model.addAttribute("March", orderService.getDataRevenueByMonthOfYear(3,year));
        model.addAttribute("April", orderService.getDataRevenueByMonthOfYear(4,year));
        model.addAttribute("May", orderService.getDataRevenueByMonthOfYear(5,year));
        model.addAttribute("June", orderService.getDataRevenueByMonthOfYear(6,year));
        model.addAttribute("July", orderService.getDataRevenueByMonthOfYear(7,year));
        model.addAttribute("August", orderService.getDataRevenueByMonthOfYear(8,year));
        model.addAttribute("September", orderService.getDataRevenueByMonthOfYear(9,year));
        model.addAttribute("October", orderService.getDataRevenueByMonthOfYear(10,year));
        model.addAttribute("November", orderService.getDataRevenueByMonthOfYear(11,year));
        model.addAttribute("December", orderService.getDataRevenueByMonthOfYear(12,year));
        return "revenue_Admin";
    }
}

