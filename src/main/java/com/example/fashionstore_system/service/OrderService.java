package com.example.fashionstore_system.service;

import com.example.fashionstore_system.entity.*;
import com.example.fashionstore_system.repository.OrderDetailRepository;
import com.example.fashionstore_system.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;


    public List<Order> getAllUserOrder(int customerId) {
        return orderRepository.findAllByCustomerId(customerId);
    }

    public void deleteOrder(int orderId) {
        orderRepository.deleteById(orderId);
    }

    public Optional<Order> getOrderById(int orderId) {
        return orderRepository.findById(orderId);
    }

    public void saveOrder(Order order) {
        orderRepository.save(order);
    }


    public Order findOrderbyOrderId(int orderId) {
        return orderRepository.getById(orderId);
    }

    public List<OrderDetail> getOrderDetailByID(int orderDetailId) {
        return orderDetailRepository.findAllByOrderId(orderDetailId);
    }

    public Order getById(int id) {
        return orderRepository.getById(id);
    }

    //phân trang
    public Page<Order> listAllOrder(int currentPage, String sortField, String sortDirection, String keyword) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(currentPage - 1, 6, sort);
        return orderRepository.findByReceiverNameContaining(keyword, pageable);
    }

//    public Order findByName(String name) {
//        return orderRepository.findByName(name);
//    }
//phân trang

    public List<Order> listAll(String keyword) {
        if (keyword != null && keyword != "") {
            List<Order> orderList = orderRepository.search(keyword);
            return orderList;
        }
        return orderRepository.findAll();
    }

}

