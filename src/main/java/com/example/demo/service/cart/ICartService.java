package com.example.demo.service.cart;

import com.example.demo.form.CartForm;
import com.example.demo.model.cart.Cart;
import com.example.demo.model.product.Product;
import com.example.demo.service.IService;

public interface ICartService extends IService<Cart> {
    Cart findCartByAppUserId(Long id);
    void addToCart(CartForm cartForm, Cart cart, Product product);
}
