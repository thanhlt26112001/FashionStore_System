package com.example.fashionstore_system.controller;

import com.example.fashionstore_system.entity.Order;
import com.example.fashionstore_system.entity.OrderDetail;
import com.example.fashionstore_system.entity.Product;
import com.example.fashionstore_system.service.CategoriesService;
import com.example.fashionstore_system.service.OrderDetailService;
import com.example.fashionstore_system.service.OrderService;
import com.example.fashionstore_system.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Controller
public class HomeController {
    @Autowired
    ProductService productService;
    @Autowired
    CategoriesService categoriesService;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderDetailService orderDetailService;


//    @RequestMapping({"/","/index"})
//    public String Homepage(Model model){
//        Page<Product> page = productService.getFeaturedProducts();
//        List<Product> allProducts = page.getContent();
//        model.addAttribute("listCategories",categoriesService.listAll());
//        model.addAttribute("keyword","");
//        model.addAttribute("listProducts",allProducts);
//        model.addAttribute("listProductsbyCategory1",productService.getProductbyCategoryId(categoriesService.listAll().get(0).getId()));
//        model.addAttribute("listProductsbyCategory2",productService.getProductbyCategoryId(categoriesService.listAll().get(1).getId()));
//        model.addAttribute("listProductsbyCategory3",productService.getProductbyCategoryId(categoriesService.listAll().get(2).getId()));
//        return "index";
//    }

    @RequestMapping({"/","/index"})
    public String Homepage(Model model){
        List<Order> orderList = orderService.getAllOrders();
        List<Product> products = productService.getAllProducts();
        for (Product product: products ) {
            product.setCount(0);
            productService.saveProduct(product);
        }
        for (Order order: orderList) {
            if (order.getStatus() == 2){
                Set<OrderDetail> orderDetailSet = order.getOrderDetails();
                for (OrderDetail orderDetail: orderDetailSet) {
                    orderDetail.getProduct().setCount(orderDetail.getProduct().getCount() + orderDetail.getQuantity());
                    productService.saveProduct(orderDetail.getProduct());
                }
            }
        }
        List<Product> productList = productService.getProductHot();
        model.addAttribute("listCategories",categoriesService.listAll());
        model.addAttribute("keyword","");
        model.addAttribute("listProducts",productList);
        model.addAttribute("listProductsbyCategory1",productService.getProductbyCategoryId(categoriesService.listAll().get(0).getId()));
        model.addAttribute("listProductsbyCategory2",productService.getProductbyCategoryId(categoriesService.listAll().get(1).getId()));
        model.addAttribute("listProductsbyCategory3",productService.getProductbyCategoryId(categoriesService.listAll().get(2).getId()));
        return "index";
    }

}
