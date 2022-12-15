package com.example.demo.service.appUserWishList;

import com.example.demo.model.AppUser;
import com.example.demo.model.AppUserWishList;
import com.example.demo.model.WishList;
import com.example.demo.model.pk.AppUserWishListPk;
import com.example.demo.repository.AppUserWishListRepository;
import com.example.demo.service.login.IAppUserService;
import com.example.demo.service.wishList.IWishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class AppUserWishListService implements IAppUserWishListService {

    @Autowired
    private AppUserWishListRepository appUserWishListRepository;

    @Autowired
    private IAppUserService appUserService;

    @Autowired
    private IWishListService wishListService;

    @Override
    public List<AppUserWishList> findAll() {
        return appUserWishListRepository.findAll();
    }

    @Override
    public AppUserWishList findById(Long id) {
        return null;
    }

    @Override
    public AppUserWishList save(AppUserWishList appUserWishList) {
        return appUserWishListRepository.save(appUserWishList);
    }

    @Override
    public void remove(Long id) {
        WishList wishList = wishListService.findById(id);
        appUserWishListRepository.deleteAppUserWishListsByWishListId(wishList.getId());
    }

    @Override
    public Optional<AppUserWishList> findByAppUserId(Long appUserId) {
        return appUserWishListRepository.findByAppUserId(appUserId);
    }

}
