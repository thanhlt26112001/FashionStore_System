package com.example.fashionstore_system.controller;

import com.example.fashionstore_system.entity.Product;
import com.example.fashionstore_system.service.CategoriesService;
import com.example.fashionstore_system.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    ProductService productService;
    @Autowired
    CategoriesService categoriesService;


    @RequestMapping({"/","/index"})
    public String Homepage(Model model){
        List<Product> allProducts = productService.getFeaturedProducts();
        model.addAttribute("listCategories",categoriesService.listAll());
        model.addAttribute("listProducts",allProducts);
        model.addAttribute("listProductsbyCategory1",productService.getProductbyCategoryId(1));
        model.addAttribute("listProductsbyCategory2",productService.getProductbyCategoryId(2));
        model.addAttribute("listProductsbyCategory3",productService.getProductbyCategoryId(3));
        model.addAttribute("listProductsbyCategory4",productService.getProductbyCategoryId(4));
        model.addAttribute("listProductsbyCategory5",productService.getProductbyCategoryId(5));

        return "index";
    }


}
