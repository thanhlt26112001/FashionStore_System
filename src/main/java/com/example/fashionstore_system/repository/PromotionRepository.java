package com.example.fashionstore_system.repository;

import com.example.fashionstore_system.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Integer> {
    public Promotion findAllById(int PromotionId);
}
