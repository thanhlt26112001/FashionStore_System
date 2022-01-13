package com.example.fashionstore_system.service;

import com.example.fashionstore_system.entity.Product;
import com.example.fashionstore_system.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
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

//phan trang
public Page<Product> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
    Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
            Sort.by(sortField).descending();

    Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
    return this.productRepository.findAll(pageable);
}

    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
public Page<Product> listAll(int currentPage){
    Pageable pageable = PageRequest.of(currentPage - 1,6);
    return this.productRepository.findAll(pageable);
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
}
