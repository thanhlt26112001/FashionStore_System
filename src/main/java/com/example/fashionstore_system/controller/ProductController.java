package com.example.fashionstore_system.controller;

import com.example.fashionstore_system.entity.Product;
import com.example.fashionstore_system.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@RequestMapping("/product")

//@RestController

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

//    @GetMapping("/list")
//    public List<Product> getAllProduct() {
//        return productService.getAllProduct();
//    }
//
//    @GetMapping("/sort")
//    public List<Product> sortAllProduct() {
//
//        return productService.sortAllProduct();
//    }
    @GetMapping("/list")
    public String getAllProduct(Model model, @RequestParam("field")Optional<String> field) {
        Sort sort = Sort.by(Sort.Direction.DESC, field.orElse("price"));
        List<Product> listproduct = productService.findAll(sort);
        model.addAttribute("listproduct", listproduct);

        return "shop";
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
