package com.example.fashionstore_system.controller;

import com.example.fashionstore_system.dto.UserDto;
import com.example.fashionstore_system.dto.UserInput;
import com.example.fashionstore_system.entity.*;
import com.example.fashionstore_system.service.CartService;
import com.example.fashionstore_system.service.ProductService;
import com.example.fashionstore_system.service.PromotionService;
import com.example.fashionstore_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private PromotionService promotionService;

    @GetMapping({"/", "/show"})
    public String checkLoginCart(Model model) throws ParseException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }
        User user = userService.findByUsername(authentication.getName());
        List<Cart> cartList = cartService.getCustomerCart(user.getCustomer().getId());
        model.addAttribute("listCart", cartList);
        double total=0.0;
        for (Cart cart : cartList){
            total+=cart.getQuantity()* Double.parseDouble(String.valueOf(cart.getProduct().getPrice()));
        }
        model.addAttribute("total",total);
        List<Promotion> allpromotion = promotionService.getAllPromotions();
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        String dayInWeek ="";
        switch (day){
            case 2:
            dayInWeek+= "Monday";
            break;
            case 3:
                dayInWeek+= "Tuesday";
                break;
            case 4:
                dayInWeek+= "Wednesday";
                break;
            case 5:
                dayInWeek+= "Thursday";
                break;
            case 6:
                dayInWeek+= "Friday";
                break;
            case 7:
                dayInWeek+= "Saturday";
                break;
            case 8:
                dayInWeek+= "Sunday";
                break;
        }
        List<Promotion> availablepromotion = new ArrayList<Promotion>() ;
        for (Promotion promotion:allpromotion){
            if (promotion.getStartDate().before(today)&&promotion.getEndDate().after(today)
                &&(promotion.getApplyDay().contains(dayInWeek)||promotion.getApplyDay().equals("AllWeek"))){
                availablepromotion.add(promotion);
            }
        }
        model.addAttribute("listPromotion",availablepromotion);
        Promotion promotion = new Promotion();
        model.addAttribute("promotion",promotion);
        return "cart";
    }
    public static String getToday(String format){
        Date date = new Date();
        return new SimpleDateFormat(format).format(date);
    }

    @PostMapping("/increaseAmount")
    public void increaseAmount(@RequestParam("cartId") Integer cartId) {
        Cart cart = cartService.getCartById(cartId);
        if (cart.getQuantity() < cart.getProduct().getQuantity()) {
            cart.setQuantity(cart.getQuantity() + 1);
        }
        cartService.saveCart(cart);
    }

    @PostMapping("/reduceAmount")
    public void reduceAmount(@RequestParam("cartId") Integer cartId) {
        Cart cart = cartService.getCartById(cartId);
        if (cart.getQuantity() > 1) {
            cart.setQuantity(cart.getQuantity() - 1);
        }
        cartService.saveCart(cart);
    }

    @RequestMapping("/deleteCart/{id}")
    public RedirectView deleteCart(@PathVariable("id") Integer id, RedirectAttributes model) {
        String alert = "Delete " + cartService.getCartById(id).getProduct().getName() + " from cart successfully!";
        cartService.deleteCart(cartService.getCartById(id));
        return new RedirectView("/cart/");
    }
    @RequestMapping("/addtoCart/{id}")
    public RedirectView addToCart(@PathVariable("id") Integer id, RedirectAttributes model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            model.addFlashAttribute("alert", "You must login first!");
            return new RedirectView("/login");
        }
        User user = userService.findByUsername(authentication.getName());
        Product product = productService.getProduct(id);
        int count = 0;
        List<Cart> customerCart = cartService.getCustomerCart(user.getCustomer().getId());
        for (Cart cart : customerCart) {
            if (cart.getProduct().equals(product) && cart.getQuantity()<product.getQuantity()) {
                cart.setQuantity(cart.getQuantity() + 1);
                cartService.saveCart(cart);
                count++;
            }else if (cart.getProduct().equals(product) && cart.getQuantity()==product.getQuantity()){
                model.addFlashAttribute("alert", "Out of max quantity");
                return new RedirectView("/cart/show");
            }
        }
        if (count == 0) {
            Cart cart = new Cart();
            cart.setQuantity(1);
            cart.setCustomer(user.getCustomer());
            cart.setProduct(product);
            cartService.saveCart(cart);
        }
        return new RedirectView("/cart/show");
    }
    @GetMapping("/checkout")
    public String checkout(Model model) {
        Order order = new Order();
        model.addAttribute("order", order);
        return "checkout_form";
    }
    @GetMapping("/applycoupon")
    public String UsePromotion(@ModelAttribute(name = "promotion") Promotion promotion,Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }
        User user = userService.findByUsername(authentication.getName());
        List<Cart> cartList = cartService.getCustomerCart(user.getCustomer().getId());
        model.addAttribute("listCart", cartList);
        double total=0.0;
        for (Cart cart : cartList){
            total+=cart.getQuantity()* Double.parseDouble(String.valueOf(cart.getProduct().getPrice()));
        }
        Promotion data = promotionService.getPromotionById(promotion.getId());
        promotion.setDiscount(data.getDiscount());
        total = total-(total*(promotion.getDiscount())/100);
        model.addAttribute("total",total);
        model.addAttribute("listPromotion",promotionService.getAllPromotions());
        model.addAttribute("promotion",promotion);
        return "cart";
    }

}
