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
import org.springframework.web.servlet.view.RedirectView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;


    public List<Order> getAllUserOrder(int customerId) {
        return orderRepository.findAllByCustomerId(customerId);
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
    public Page<Order> listAllOrder(int currentPage, String sortField, String sortDirection, String keyword, Date startDate, Date endDate) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(currentPage - 1, 6, sort);
        return orderRepository.findByReceiverNameContaining(keyword, pageable);
    }

    public Page<Order> sortByTime(int currentPage, String keyword, Date startDate, Date endDate) {
        Pageable pageable = PageRequest.of(currentPage - 1, 6);
        if (startDate != null && endDate != null) {
            return orderRepository.findAllOrderByTime(startDate, endDate, keyword, pageable);
        }
        if (startDate == null && endDate != null) {
            return orderRepository.findEndOrderByTime(endDate, keyword, pageable);
        }
        if (endDate == null && startDate != null) {
            return orderRepository.findStartOrderByTime(startDate, keyword, pageable);
        }
        return orderRepository.findByReceiverNameContaining(keyword, pageable);
    }

    public Page<Order> listOrderByMonthAndYear(int currentPage, int month, int year) {
        Pageable pageable = PageRequest.of(currentPage - 1, 6);
        return orderRepository.findByMonthAndYear(month, year, pageable);
    }

    public List<Order> listAll(String keyword) {
        if (keyword != null && keyword != "") {
            List<Order> orderList = orderRepository.search(keyword);
            return orderList;
        }
        return orderRepository.findAll();
    }

    public Integer getDataRevenueByMonthOfYear(int month, int year) {
        List<Order> orderList = orderRepository.findAll();
        Integer revenue = 0;
        for (Order order : orderList) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(order.getCreatedAt());
            if ((calendar.get(Calendar.MONTH) + 1) == month && calendar.get(Calendar.YEAR) == year && order.getStatus() == 2) {
                revenue += order.getPrice().intValue();
            }
        }
        return revenue;
    }

    public List<Order> getLastestOrders() {
        return orderRepository.getRecentOrder();

    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}

