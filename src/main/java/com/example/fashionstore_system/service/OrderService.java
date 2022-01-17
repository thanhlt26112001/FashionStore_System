package com.example.fashionstore_system.service;

import com.example.fashionstore_system.entity.Order;
import com.example.fashionstore_system.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;


    public List<Order> getAllUserOrder(int customerId) {
        return  orderRepository.findAllByCustomerId(customerId);
    }
    public void deleteOrder(int orderId){
         orderRepository.deleteById(orderId);
    }
    public Optional<Order> getOrderById(int orderId){
        return  orderRepository.findById(orderId);
    }

    public void saveOrder(Order order){
         orderRepository.save(order);
    }
    public Order findOrderbyOrderId(int orderId){
        return  orderRepository.getById(orderId);
    }
}
