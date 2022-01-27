package com.example.fashionstore_system.controller;

import com.example.fashionstore_system.entity.Order;
import com.example.fashionstore_system.entity.OrderDetail;
import com.example.fashionstore_system.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class ChartController {
    @Autowired
    private OrderService orderService;

    @RequestMapping("/admin/revenue")
    public String getPieChart(@RequestParam(name = "Year", defaultValue = "2022") int Year, Model model) {
        Map<String, Integer> graphData = new TreeMap<>();
        model.addAttribute("Year", Year);
        graphData.put("12", orderService.getDataRevenueByMonthOfYear(12, Year));
        graphData.put("11", orderService.getDataRevenueByMonthOfYear(11, Year));
        graphData.put("10", orderService.getDataRevenueByMonthOfYear(10, Year));
        graphData.put("9", orderService.getDataRevenueByMonthOfYear(9, Year));
        graphData.put("8", orderService.getDataRevenueByMonthOfYear(8, Year));
        graphData.put("7", orderService.getDataRevenueByMonthOfYear(7, Year));
        graphData.put("6", orderService.getDataRevenueByMonthOfYear(6, Year));
        graphData.put("5", orderService.getDataRevenueByMonthOfYear(5, Year));
        graphData.put("4", orderService.getDataRevenueByMonthOfYear(4, Year));
        graphData.put("3", orderService.getDataRevenueByMonthOfYear(3, Year));
        graphData.put("2", orderService.getDataRevenueByMonthOfYear(2, Year));
        graphData.put("1", orderService.getDataRevenueByMonthOfYear(1, Year));

        model.addAttribute("chartData", graphData);
        return "revenue_Admin";
    }

    @RequestMapping("/admin/revenue/order/{month}/{year}")
    public String viewOrder(Model model,
                            @PathVariable(name = "month") int month,
                            @PathVariable(name = "year") int year) {
        return orderListByMonthAndYear(1, month, year, model);
    }

    @GetMapping("/admin/revenue/order/{pageNumber}")
    public String orderListByMonthAndYear(@PathVariable(name = "pageNumber") int currentPage,
                                   @PathVariable(name = "month") int month,
                                   @PathVariable(name = "year") int year,
                                   Model model) {
        Page<Order> page = orderService.listOrderByMonthAndYear(currentPage, month, year);
        long totalItems = page.getTotalElements();
        int totalPages = page.getTotalPages();
        List<Order> orderList = page.getContent();
        model.addAttribute("month", month);
        model.addAttribute("year", year);
        model.addAttribute("orderList", orderList);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("totalPages", totalPages);
        return "listOrderRevenueAdmin";
    }

//    @GetMapping("/orderByTime")
//    public String viewOrderByTime(Model model) {
//        List<Order> orderList = orderService.getAllOrders();
//        model.addAttribute("orderList", orderList);
//        return "listOrderByTimeAdmin";
//    }

    @RequestMapping("/admin/orderListByTime")
    public RedirectView viewOrderList(@RequestParam(name = "startDate") String startDate,
                                      @RequestParam(name = "endDate") String endDate,
                                      RedirectAttributes model) throws ParseException {
        Date start = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
        Date end = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
        if (start.after(end)) {
            model.addFlashAttribute("alert_order", "Start Date must before End Date!!!");
            return new RedirectView("admin/order");
        }
        List<Order> orderList = orderService.getAllOrders();
        List<Order> orderListByDate = new ArrayList<>();
        for (Order order : orderList) {
            Date date = order.getCreatedAt();
            if (start.before(date) && end.after(date)) {
                orderListByDate.add(order);
            }
        }
        model.addAttribute("orderList", orderListByDate);
        return new RedirectView("/admin/orderByTime");
    }
}
