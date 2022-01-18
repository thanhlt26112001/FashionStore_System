package com.example.fashionstore_system.repository;

import com.example.fashionstore_system.entity.Category;
import com.example.fashionstore_system.entity.Feedback;
import com.example.fashionstore_system.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    public Page<Feedback> findAll( Pageable pageable);
    public List<Feedback> findAllByCustomerId(Integer customerId);
    public Page<Feedback> findAllByProductId(Integer productId, Pageable pageable);
}
