package com.example.fashionstore_system.service;

import com.example.fashionstore_system.entity.Product;
import com.example.fashionstore_system.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
//    public List<Product> getAllProduct() {
//        return productRepository.findAll();
//    }

//    public List<Product> sortAllProduct(){
//        return productRepository.findAllByOrderByPriceDesc();
//    }


    public List<Product> findAllByOrderByPriceDesc() {
        return productRepository.findAllByOrderByPriceDesc();
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public List<Product> findAll(Sort sort) {
        return productRepository.findAll(sort);
    }


    // product_detail
    public Product getProduct(int id) {
        return productRepository.getById(id);
    }

    //search for products by name
    public <String> List<Product> listAll(String keyword){
        if(keyword != null){
            return productRepository.search(keyword);
        }
        return productRepository.findAll();
    }
}
