package com.example.demo.repository;

import com.example.demo.model.AppUserWishList;
import com.example.demo.model.pk.AppUserWishListPk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppUserWishListRepository extends JpaRepository<AppUserWishList, AppUserWishListPk> {

    @Query(value = "SELECT p FROM #{#entityName} p JOIN FETCH p.id.appUser JOIN FETCH p.id.wishList WHERE p.id.appUser.id = :appUserId")
    List<AppUserWishList> findByAppUserId(@Param("appUserId") Long appUserId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM app_user_wish_list WHERE wish_list_id = :wishListId", nativeQuery = true)
    void deleteAppUserWishListsByWishListId(@Param("wishListId") Long wishListId);
}
