package com.example.fashionstore_system.service;

import com.example.fashionstore_system.entity.Product;
import com.example.fashionstore_system.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    public List<Product> sortAllProduct(){
        return productRepository.findAllByOrderByPriceDesc();
    }
}
