package com.example.demo.service.wishList;

import com.example.demo.model.AppUser;
import com.example.demo.model.AppUserWishList;
import com.example.demo.model.Product;
import com.example.demo.model.WishList;
import com.example.demo.model.pk.AppUserWishListPk;
import com.example.demo.repository.WishListRepository;
import com.example.demo.service.appUserWishList.IAppUserWishListService;
import com.example.demo.service.login.IAppUserService;
import com.example.demo.service.product.IProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class WishListService implements IWishListService {

    @Autowired
    private WishListRepository wishListRepository;

    @Autowired
    private IAppUserWishListService appUserWishListService;

    @Autowired
    private IAppUserService appUserService;

    @Autowired
    private IProductService productService;

    @Override
    public List<WishList> findAll() {
        return wishListRepository.findAll();
    }

    @Override
    public WishList findById(Long id) {
        return wishListRepository.findById(id).orElse(null);
    }

    @Override
    public WishList save(WishList wishList) {
        return wishListRepository.save(wishList);
    }

    @Override
    public void remove(Long id) {
        wishListRepository.deleteById(id);
    }

    @Override
    public void addWistList(Long productId) {
        AppUser appUser = appUserService.getCurrentUser();

        if (appUser != null) {
            WishList wishList = new WishList();
            Product product = productService.findById(productId);
            wishList.setProduct(product);
            this.save(wishList);

            AppUserWishList appUserWishList = new AppUserWishList();
            AppUserWishListPk appUserWishListPk = new AppUserWishListPk();
            appUserWishListPk.setWishList(wishList);
            appUserWishListPk.setAppUser(appUser);

            appUserWishList.setId(appUserWishListPk);
            appUserWishListService.save(appUserWishList);
        }

    }

}
