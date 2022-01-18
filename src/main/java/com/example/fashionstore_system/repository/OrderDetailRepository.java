package com.example.fashionstore_system.repository;

import com.example.fashionstore_system.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    public List<OrderDetail> findAllByOrderId(int orderId);
    public void deleteByOrderId(int orderId);

}
