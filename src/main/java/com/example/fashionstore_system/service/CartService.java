package com.example.fashionstore_system.service;

import com.example.fashionstore_system.entity.Cart;
import com.example.fashionstore_system.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    public List<Cart> getCustomerCart(int customerId){
        return cartRepository.findByCustomer_Id(customerId);
    }

    public Cart getCartById(int cartId){
        return cartRepository.getById(cartId);
    }

    public void saveCart(Cart cart){
        cartRepository.save(cart);
    }

    public void deleteCart(Cart cart){
        cartRepository.delete(cart);
    }

    public void deleteUserCart(List<Cart> cartList) {
        for(Cart cart:cartList){
            cartRepository.delete(cart);
        }
    }
}
