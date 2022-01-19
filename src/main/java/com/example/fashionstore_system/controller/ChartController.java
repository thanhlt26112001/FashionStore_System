package com.example.fashionstore_system.controller;

import com.example.fashionstore_system.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.TreeMap;

@Controller
public class ChartController {
    @Autowired
    private OrderService orderService;

    //    @RequestMapping("/linechartdata")
//    public String getDataFromDB(){
//        List<Order> dataList = orderRepository.findAll();
//        JsonArray jsonArrayMonth = new JsonArray();
//        JsonArray jsonArrayPrice = new JsonArray();
//        JsonObject jsonObject = new JsonObject();
//        SimpleDateFormat formatter = new SimpleDateFormat("MM/YYYY");
//        dataList.forEach(data->{
//            String strDate = formatter.format(data.getCreatedAt());
//            jsonArrayMonth.add(data.);
//            jsonArrayPrice.add(data.getPrice());
//        });
//        jsonObject.add("monthofyear", jsonArrayMonth);
//        jsonObject.add("price", jsonArrayPrice);
//        return jsonObject.toString();
//    }
    @RequestMapping("/admin/revenue")
    public String getPieChart(@RequestParam(name = "Year",defaultValue = "2022") int Year,Model model) {
        Map<String, Integer> graphData = new TreeMap<>();
        model.addAttribute("Year", Year);
        graphData.put("12", orderService.getDataRevenueByMonthOfYear(12,Year));
        graphData.put("11", orderService.getDataRevenueByMonthOfYear(11,Year));
        graphData.put("10", orderService.getDataRevenueByMonthOfYear(10,Year));
        graphData.put("9", orderService.getDataRevenueByMonthOfYear(9,Year));
        graphData.put("8", orderService.getDataRevenueByMonthOfYear(8,Year));
        graphData.put("7", orderService.getDataRevenueByMonthOfYear(7,Year));
        graphData.put("6", orderService.getDataRevenueByMonthOfYear(6,Year));
        graphData.put("5", orderService.getDataRevenueByMonthOfYear(5,Year));
        graphData.put("4", orderService.getDataRevenueByMonthOfYear(4,Year));
        graphData.put("3", orderService.getDataRevenueByMonthOfYear(3,Year));
        graphData.put("2", orderService.getDataRevenueByMonthOfYear(2,Year));
        graphData.put("1", orderService.getDataRevenueByMonthOfYear(1,Year));

        model.addAttribute("chartData", graphData);
        return "revenue_Admin";
    }
}
