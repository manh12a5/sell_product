package com.example.demo.repository;

import com.example.demo.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {
    Cart findCartByAppUserId(Long id);

    @Query(value = "SELECT * FROM cart WHERE cart.app_user_id IS NULL", nativeQuery = true)
    List<Cart> findGuestCart();
}
