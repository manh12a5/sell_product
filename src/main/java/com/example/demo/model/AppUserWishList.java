package com.example.demo.model;

import com.example.demo.model.pk.AppUserWishListPk;
import lombok.*;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "appUser_wishList")
public class AppUserWishList {

    @EmbeddedId
    private AppUserWishListPk appUserWishListId;

}
