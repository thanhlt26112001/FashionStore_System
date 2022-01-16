package com.example.fashionstore_system.repository;

import com.example.fashionstore_system.entity.Category;
import com.example.fashionstore_system.entity.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    public Page<Category> findByNameContaining(String keyword, Pageable pageable);

    public Category findByName(String name);

    public Category findById(int id);
}
