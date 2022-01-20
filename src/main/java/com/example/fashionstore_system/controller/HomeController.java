package com.example.fashionstore_system.controller;

import com.example.fashionstore_system.entity.Product;
import com.example.fashionstore_system.service.CategoriesService;
import com.example.fashionstore_system.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
        Page<Product> page = productService.getFeaturedProducts();
        List<Product> allProducts = page.getContent();
        model.addAttribute("listCategories",categoriesService.listAll());
        model.addAttribute("keyword","");
        model.addAttribute("listProducts",allProducts);
        model.addAttribute("listProductsbyCategory1",productService.getProductbyCategoryId(categoriesService.listAll().get(0).getId()));
        model.addAttribute("listProductsbyCategory2",productService.getProductbyCategoryId(categoriesService.listAll().get(1).getId()));
        model.addAttribute("listProductsbyCategory3",productService.getProductbyCategoryId(categoriesService.listAll().get(2).getId()));
        return "index";
    }


}
