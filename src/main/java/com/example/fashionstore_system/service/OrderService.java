package com.example.fashionstore_system.service;

import com.example.fashionstore_system.entity.Order;
import com.example.fashionstore_system.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;


    public List<Order> getAllUserOrder(int customerId) {
        return  orderRepository.findAllByCustomerId(customerId);
    }
}
