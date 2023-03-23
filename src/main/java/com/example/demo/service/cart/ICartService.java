package com.example.demo.service.cart;

import com.example.demo.form.CartForm;
import com.example.demo.model.Cart;
import com.example.demo.model.Product;
import com.example.demo.service.IService;

public interface ICartService extends IService<Cart> {
    Cart findCartByAppUserId(Long id);
    void addToCart(CartForm cartForm, Cart cart, Product product);
    void removeGuestCart();
    Cart findCartByEmailOfAppUser(String email);
}
