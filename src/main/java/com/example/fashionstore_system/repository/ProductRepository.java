package com.example.fashionstore_system.repository;

import com.example.fashionstore_system.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("SELECT p FROM Product p WHERE p.name LIKE %?1%")
    public Page<Product> searchProductByName(String keyword, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %?1% AND p.category.id=?2")
    public Page<Product> searchProductByNameAndCategory(String keyword,int categoryId, Pageable pageable);

    public Page<Product> findProductByName(String keyword, Pageable pageable);

    public Product findProductByName(String name);
}
