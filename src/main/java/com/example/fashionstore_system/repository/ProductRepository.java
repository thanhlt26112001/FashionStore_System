package com.example.fashionstore_system.repository;

import com.example.fashionstore_system.entity.Customer;
import com.example.fashionstore_system.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("SELECT p FROM Product p WHERE p.name LIKE %?1%")
    public Page<Product> searchProductByName(String keyword, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %?1% AND p.category.id=?2")
    public Page<Product> searchProductByNameAndCategory(String keyword,int categoryId, Pageable pageable);

    public Page<Product> findByNameContaining(String keyword, Pageable pageable);

    public Page<Product> findProductByName(String keyword, Pageable pageable);

    public Product findProductByName(String name);

    @Query(value = "SELECT * FROM Products where status = 1 order by id desc limit 8",nativeQuery = true)
    public List<Product> featuredProduct();
    @Query(value = "SELECT * FROM Products where category_id = ? limit 4",nativeQuery = true)
    public List<Product> getProductByCategoryId(int CategoryId);
}
