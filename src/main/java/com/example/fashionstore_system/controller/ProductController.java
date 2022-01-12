package com.example.fashionstore_system.controller;

import com.example.fashionstore_system.entity.Product;
import com.example.fashionstore_system.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RequestMapping("/product")

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/list")
    public List<Product> getAllProduct() {
        return productService.getAllProduct();
    }

    @GetMapping("/sort")
    public List<Product> sortAllProduct() {

        return productService.sortAllProduct();
    }

//    @RequestMapping("/edit/{id}")
//    public ModelAndView showEditProductPage(@PathVariable(name = "id") int id) {
//        ModelAndView mav = new ModelAndView("edit_product");
//        Product product = productService.get(id);
//        mav.addObject("product", product);
//
//        return mav;
//    }
    @GetMapping("/Productdetail/{id}")
    public String showProductDetail(Model model, @PathVariable(name = "id") int id) {

        Product product = productService.getProduct(id);
        model.addAttribute("product", product);

        return "product-details";
    }
}
