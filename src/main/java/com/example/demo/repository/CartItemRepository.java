package com.example.demo.repository;

import com.example.demo.model.cart.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    List<CartItem> findCartItemByCartId(Long cartId);
    void deleteCartItemByCartId(Long cartId);
}
