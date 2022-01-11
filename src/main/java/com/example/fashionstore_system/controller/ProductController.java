package com.example.fashionstore_system.controller;

import com.example.fashionstore_system.entity.Product;
import com.example.fashionstore_system.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/product")
@RestController
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
}
