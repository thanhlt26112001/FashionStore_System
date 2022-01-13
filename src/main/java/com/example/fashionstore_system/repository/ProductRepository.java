package com.example.fashionstore_system.repository;

import com.example.fashionstore_system.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findAllByOrderByPriceDesc();

    //search for products by name
    @Query("SELECT p. From Product p WHERE p.name lIKE %?1%")
    public List<Product> search(String keyword);
}
