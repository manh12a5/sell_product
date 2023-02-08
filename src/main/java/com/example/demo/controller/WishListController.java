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

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/wishlist")
public class WishListController {

    @Autowired
    private IAppUserService appUserService;

    @Autowired
    private IAppUserWishListService appUserWishListService;

    @Autowired
    private IWarehouseService warehouseService;

    @Autowired
    private IWishListService wishListService;

    @GetMapping("")
    public ModelAndView wishList() {
        ModelAndView modelAndView = new ModelAndView("view/wishlist");
        List<WishList> wishLists = new ArrayList<>();
        List<WarehouseForm> warehouse = new ArrayList<>();
        int inStock = 0;

        AppUser appUser = appUserService.getCurrentUser();
        if (appUser != null) {
            List<AppUserWishList> appUserWishLists = appUserWishListService.findByAppUserId(appUser.getId());

            if (appUserWishLists != null) {
                for (AppUserWishList appUserWishList: appUserWishLists) {
                    WishList wishList = appUserWishList.getAppUserWishListId().getWishList();
                    wishLists.add(wishList);

                    warehouse = warehouseService.findWarehouseByProductId(wishList.getProduct().getId());
                    for (WarehouseForm warehouseForm: warehouse) {
                        inStock += warehouseForm.getStock();
                    }
                }
            }
        }

        modelAndView.addObject("inStock", inStock);
        modelAndView.addObject("wishLists", wishLists);
        modelAndView.addObject("warehouse", warehouse);

        return modelAndView;
    }

    @GetMapping("/addWishlist/{productId}")
    public String addWishList(@PathVariable Long productId) {
        String url = "redirect:/login";

        if (appUserService.getCurrentUser() != null) {
            wishListService.addWistList(productId);
            url = "redirect:/wishlist";
        }

        return url;
    }

    @GetMapping("/deleteWishlist/{id}")
    public String deleteWishlist(@PathVariable Long id) {
        appUserWishListService.remove(id);
        wishListService.remove(id);

        return "redirect:/wishlist";
    }

}
