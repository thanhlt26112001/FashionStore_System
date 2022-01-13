package com.example.fashionstore_system.repository;

import com.example.fashionstore_system.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    //List<Product> findAllByOrderByPriceDesc();
//    public Page<Product> findProductByNameContaining(String name, Pageable pageable);
//    public Page<Product> findProductsByCategoryOrOrderByIdDesc(Integer category, Pageable pageable);
//    public Page<Product> findProductsByCategoryAAndNameContaining(Integer category_id, String name, Pageable pageable);

}
