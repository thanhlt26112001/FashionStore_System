package com.example.fashionstore_system.repository;

import com.example.fashionstore_system.entity.SizeProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SizeProductRepository extends JpaRepository<SizeProduct, Integer> {
}
