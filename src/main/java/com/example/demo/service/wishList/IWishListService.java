package com.example.demo.service.wishList;

import com.example.demo.model.WishList;
import com.example.demo.service.IService;

public interface IWishListService extends IService<WishList> {
    void addWistList(Long productId);
}
