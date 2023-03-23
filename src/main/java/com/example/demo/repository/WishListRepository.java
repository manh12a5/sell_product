package com.example.demo.repository;

import com.example.demo.model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM wish_list WHERE id = :wishListId", nativeQuery = true)
    void deleteWishListById(@Param("wishListId") Long wishListId);

}
