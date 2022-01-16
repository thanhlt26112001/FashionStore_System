package com.example.fashionstore_system.service;

import com.example.fashionstore_system.entity.OrderDetail;
import com.example.fashionstore_system.repository.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    public List<OrderDetail> getOrderDetailsByOrderId(int orderId) {
        return orderDetailRepository.findAllByOrderId(orderId);
    }

    public void deleteorderdetail(List<OrderDetail> list) {
        for (OrderDetail orderDetail : list) {
            orderDetailRepository.delete(orderDetail);
        }
    }
}
