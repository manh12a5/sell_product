package com.example.demo.service.cartItem;

import com.example.demo.model.CartItem;
import com.example.demo.service.IService;

import java.util.List;

public interface ICartItemService extends IService<CartItem> {
    void createCartItem(CartItem cartItem);
    List<CartItem> findCartItemByCartId(Long cartId);
    void deleteCartItemByCartId(Long cartId);
}
