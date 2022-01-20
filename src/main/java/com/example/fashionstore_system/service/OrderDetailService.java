package com.example.fashionstore_system.service;

import com.example.fashionstore_system.entity.OrderDetail;
import com.example.fashionstore_system.repository.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class OrderDetailService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    public List<OrderDetail> getOrderDetailsByOrderId(int orderId) {
        return orderDetailRepository.findAllByOrderId(orderId);
    }

    public Set<OrderDetail> getOrderDetailsByProductId(int productId) {
        return orderDetailRepository.findAllByProduct_Id(productId);
    }

    public void deleteorderdetail(OrderDetail orderDetail) {
        orderDetailRepository.delete(orderDetail);
    }
    public void saveOrderDetail(OrderDetail orderDetail){
        orderDetailRepository.save(orderDetail);
    }
}
