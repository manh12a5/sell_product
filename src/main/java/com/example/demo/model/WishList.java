package com.example.demo.model;

import lombok.*;

import javax.persistence.*;
import java.util.Collection;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WishList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Product product;

    @OneToMany(mappedBy = "appUserWishListId.wishList", fetch = FetchType.LAZY)
    private Collection<AppUserWishList> appUserWishListCollection;
}
