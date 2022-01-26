package com.example.fashionstore_system.controller;

import com.example.fashionstore_system.entity.Order;
import com.example.fashionstore_system.entity.OrderDetail;
import com.example.fashionstore_system.entity.Product;
import com.example.fashionstore_system.entity.User;
import com.example.fashionstore_system.service.*;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CartService cartService;

    @GetMapping("/myorder")
    public String listorder(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }
        User user = userService.findByUsername(authentication.getName());
        int customerId= user.getCustomer().getId();
        model.addAttribute("orderlist",orderService.getAllUserOrder(customerId));
        model.addAttribute("size_carts", cartService.getCartSize());
        return "user_order";
    }
    @RequestMapping("/myorder/{id}")
    public String vieworderdetails(@PathVariable("id") Integer id, Model model){
        model.addAttribute("orderdetail",orderDetailService.getOrderDetailsByOrderId(id));
        model.addAttribute("order",orderService.findOrderbyOrderId(id));
        model.addAttribute("size_carts", cartService.getCartSize());
        return "order_detail";
    }
    @RequestMapping("/myorder/cancel/{id}")
    public String CancelOrders(@PathVariable("id") Integer id, Model model){
        List<OrderDetail> orderDetails= orderDetailService.getOrderDetailsByOrderId(id);
        for(OrderDetail orderDetail:orderDetails){
            Product product= productService.getProduct(orderDetail.getProduct().getId());
            product.setQuantity(product.getQuantity()+orderDetail.getQuantity());
        }
        Order order= orderService.findOrderbyOrderId(id);
        order.setStatus(3);
        orderService.saveOrder(order);
        return "redirect:/myorder";
    }
}
