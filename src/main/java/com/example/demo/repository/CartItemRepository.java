package com.example.demo.repository;

import com.example.demo.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    List<CartItem> findCartItemByCartId(Long cartId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM cart_item WHERE cart_item.cart_id = ?", nativeQuery = true)
    void deleteCartItemByCartId(Long cartId);
}
