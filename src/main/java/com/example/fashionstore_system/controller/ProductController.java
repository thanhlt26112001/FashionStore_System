package com.example.fashionstore_system.controller;

import com.example.fashionstore_system.entity.Category;
import com.example.fashionstore_system.entity.Customer;
import com.example.fashionstore_system.entity.Feedback;
import com.example.fashionstore_system.entity.Product;
import com.example.fashionstore_system.repository.OrderRepository;
import com.example.fashionstore_system.service.CustomerService;
import com.example.fashionstore_system.service.FeedbackService;
import com.example.fashionstore_system.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.List;
import java.util.Optional;

@RequestMapping("/product")
@Controller
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private FeedbackService feedbackService;
    @Autowired
    CustomerService customerService;
    @Autowired
    OrderRepository orderRepository;

    @GetMapping("/listproducts")
    public String viewCourse(Model model,
                             @RequestParam(value = "keyword", defaultValue = "") String keyword,
                             @RequestParam(value = "categoryId", defaultValue = "-1") Integer categoryId,
                             @RequestParam(value = "sortField", defaultValue = "id") String sortField,
                             @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir) {
        return listByPages(1, sortField, sortDir, keyword, categoryId, model);
    }

    //phân trang
    @GetMapping("/listproducts/{pageNumber}")
    public String listByPages(@PathVariable(name = "pageNumber") int currentPage,
                              @RequestParam("sortField") String sortField,
                              @RequestParam("sortDir") String sortDir,
                              @RequestParam("keyword") String keyword,
                              @RequestParam(value = "categoryId") Integer categoryId,
                              Model model) {
        Page<Product> page = productService.listAll(currentPage, sortField, sortDir, keyword, categoryId);
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
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("query", "?sortField=" + sortField + "&sortDir="
                + sortDir + "&keyword=" + keyword + "&categoryId=" + categoryId);
        model.addAttribute("categoryList", productService.getCategoryList());
        return "shop";
    }

//    @GetMapping("/productdetail/{id}")
//    public String showProductDetail(Model model, @PathVariable(name = "id") int id) {
//        return "product-details";
//    }
    //function view feedbackList
    @GetMapping("/productdetail/{id}")
    public String viewFeedback(Model model,@PathVariable(name = "id") int currentProduct ) {
        return listByPagesCategory(1,currentProduct, model);
    }
    @GetMapping("/productdetail/{id}/{pageNumber}")
    public String listByPagesCategory(@PathVariable(name = "pageNumber") int currentPage,
                                      @PathVariable(name = "id") int currentProduct, Model model) {
        Product product = productService.getProduct(currentProduct);
        model.addAttribute("product", product);
        Page<Feedback> page = feedbackService.listAllFeedback(product.getId(),currentPage);
        long totalItems = page.getTotalElements();
        int totalPages = page.getTotalPages();
        List<Feedback> feedbackList = page.getContent();
        model.addAttribute("feedbackList", feedbackList);
        model.addAttribute("currentPage", currentPage);
        return "product-details";
    }
    //function createFeedback
    @GetMapping("/feedback/createFeedback")
    public String createFeedbeck(Model model) {
        Feedback feedback = new Feedback();
        model.addAttribute("feedback", feedback);
        return "product-details";
    }
    //function saveFeedback
    @PostMapping("/feedback/saveFeedback")
    public RedirectView saveFeedback(@Valid Feedback feedback, RedirectAttributes model) {
        feedback.setContent(feedback.getContent());
        feedback.setImage(feedback.getImage());
        Customer customer = feedback.getCustomer();
        customer.setName(feedback.getCustomer().getName());
        if (feedbackService.finAllCustomerFeedback(customer.getId()) == null) {
            model.addFlashAttribute("alert_feedback", "Please order for feedback!");
            return new RedirectView("/feedback/createFeedback");
        }
        customerService.saveCustomer(customer);
        feedback.setCustomer(customer);
        feedbackService.saveFeedback(feedback);
        model.addAttribute("feedback", feedback);
        return new RedirectView("/feedback/feedbackList");
    }
}