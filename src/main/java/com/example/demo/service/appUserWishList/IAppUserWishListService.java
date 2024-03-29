package com.example.demo.service.appUserWishList;

import com.example.demo.model.AppUserWishList;
import com.example.demo.model.pk.AppUserWishListPk;
import com.example.demo.service.IService;

import java.util.List;
import java.util.Optional;

public interface IAppUserWishListService extends IService<AppUserWishList> {
    List<AppUserWishList> findByAppUserId(Long appUserId);
}
