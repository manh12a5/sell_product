package com.example.demo.repository;

import com.example.demo.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {
    Cart findCartByAppUserId(Long id);

    @Query(value = "SELECT * FROM cart WHERE cart.app_user_id IS NULL", nativeQuery = true)
    List<Cart> findGuestCart();

    @Query(value = "SELECT * FROM cart LEFT JOIN app_user ON app_user.id = cart.app_user_id WHERE app_user.email = :email",
            nativeQuery = true)
    Cart findCartByEmailOfAppUser(@Param("email") String email);
}
