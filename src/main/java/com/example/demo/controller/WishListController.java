package com.example.demo.controller;

import com.example.demo.form.WarehouseForm;
import com.example.demo.model.AppUser;
import com.example.demo.model.AppUserWishList;
import com.example.demo.model.WishList;
import com.example.demo.service.appUserWishList.IAppUserWishListService;
import com.example.demo.service.login.IAppUserService;
import com.example.demo.service.warehouse.IWarehouseService;
import com.example.demo.service.wishList.IWishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/wishlist")
public class WishListController {

    @Autowired
    private IAppUserService appUserService;

    @Autowired
    private IAppUserWishListService appUserWishListService;

    @Autowired
    private IWishListService wishListService;

    @Autowired
    private IWarehouseService warehouseService;

    @GetMapping("")
    public ModelAndView wishList() {
        ModelAndView modelAndView = new ModelAndView("view/wishlist");
        WishList wishList = null;
        List<WarehouseForm> warehouse = null;
        int inStock = 0;

        AppUser appUser = appUserService.getCurrentUser();
        if (appUser != null) {
            AppUserWishList appUserWishList = appUserWishListService.findByAppUserId(appUser.getId()).orElse(null);

            if (appUserWishList != null) {
                wishList = wishListService.findById(appUserWishList.getAppUserWishListId().getWishList().getId());
                warehouse = warehouseService.findWarehouseByProductId(wishList.getProduct().getId());

                for (WarehouseForm warehouseForm: warehouse) {
                    inStock += warehouseForm.getStock();
                }
            }
        }

        modelAndView.addObject("inStock", inStock);
        modelAndView.addObject("wishList", wishList);
        modelAndView.addObject("warehouse", warehouse);

        return modelAndView;
    }

    @GetMapping("/addWishlist/{productId}")
    public String addWishList(@PathVariable Long productId) {
        if (appUserService.getCurrentUser() != null) {
            wishListService.addWistList(productId);
        }
        return "redirect:/wishlist";
    }

    @GetMapping("/deleteWishlist/{id}")
    public String deleteWishlist(@PathVariable Long id) {
        appUserWishListService.remove(id);
        wishListService.remove(id);

        return "redirect:/wishlist";
    }

}
