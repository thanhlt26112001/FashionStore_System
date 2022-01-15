package com.example.fashionstore_system.controller;

import com.example.fashionstore_system.dto.UserDto;
import com.example.fashionstore_system.dto.UserInput;
import com.example.fashionstore_system.entity.Cart;
import com.example.fashionstore_system.entity.User;
import com.example.fashionstore_system.service.CartService;
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

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;

    @GetMapping({"/","/show"})
    public String checkLoginCart(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }
        User user = userService.findByUsername(authentication.getName());
        model.addAttribute("listCart",cartService.getCustomerCart(user.getCustomer().getId()));
        return "cart";
    }

    @PostMapping("/increaseAmount")
    public void increaseAmount(@RequestParam("cartId") Integer cartId){
        Cart cart = cartService.getCartById(cartId);
        if(cart.getQuantity()<cart.getProduct().getQuantity()){
            cart.setQuantity(cart.getQuantity()+1);
        }
        cartService.saveCart(cart);
    }

    @PostMapping("/reduceAmount")
    public void reduceAmount(@RequestParam("cartId") Integer cartId){
        Cart cart = cartService.getCartById(cartId);
        if(cart.getQuantity()>1){
            cart.setQuantity(cart.getQuantity()-1);
        }
        cartService.saveCart(cart);
    }

    @RequestMapping("/deleteCart/{id}")
    public RedirectView deleteCart(@PathVariable("id") Integer id, RedirectAttributes model){
        String alert = "Delete "+cartService.getCartById(id).getProduct().getName()+" from cart successfully!";
        cartService.deleteCart(cartService.getCartById(id));
        return new RedirectView("/cart/");
    }

}
