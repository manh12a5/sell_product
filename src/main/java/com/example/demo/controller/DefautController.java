package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.appUserWishList.IAppUserWishListService;
import com.example.demo.service.cart.ICartService;
import com.example.demo.service.category.ICategoryService;
import com.example.demo.service.login.IAppUserService;
import com.example.demo.service.product.IProductService;
import com.example.demo.service.warehouse.IWarehouseService;
import com.example.demo.service.wishList.IWishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("")
public class DefautController {

    @Autowired
    private ICategoryService categoryServiceImp;

    @Autowired
    private IAppUserService appUserService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IProductService productService;

    @Autowired
    private ICartService cartService;

    @Autowired
    private IWishListService wishListService;

    @Autowired
    private IAppUserWishListService appUserWishListService;

    @Autowired
    private IWarehouseService warehouseService;

    @RequestMapping("")
    public ModelAndView home(@PageableDefault(size = 5) Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("view/index");

        modelAndView.addObject("categories", categoryServiceImp.findAll());
        modelAndView.addObject("top5products", productService.findTop5ByOrderByPriceDesc(pageable));
        return modelAndView;
    }

    @RequestMapping("/about")
    public ModelAndView about(){
        return new ModelAndView("/view/about");
    }

    @RequestMapping("/checkout")
    public ModelAndView checkout(){
        return new ModelAndView("/view/checkout");
    }

    @RequestMapping("/contact")
    public ModelAndView contact(){
        return new ModelAndView("/view/contact-us");
    }

    @PostMapping("/search")
    public ModelAndView showSearchNameProduct(@RequestParam String name, @PageableDefault(size = 6) Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("view/index");
        String nameProduct = "%" + name + "%";
        Page<Product> productList = productService.findProductByName(nameProduct, pageable);
        modelAndView.addObject("products", productList);
        return modelAndView;
    }

    @GetMapping("/searchCategory/{id}")
    public ModelAndView showSearchCategory(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("view/shop");
        modelAndView.addObject("products", productService.findById(id));
        return modelAndView;
    }

    @PostMapping("/searchcategory/{id}")
    public ModelAndView searchProductByCategory(@PathVariable Long id, @PageableDefault(size = 6) Pageable pageable) {
        Page<Product> productPage = productService.findProductByCategoryName(id, pageable);
        return new ModelAndView("product/search-category", "categories", productPage);
    }

    @RequestMapping("/my-account")
    public ModelAndView myAccount(){
        return new ModelAndView("/view/my-account");
    }

    @RequestMapping("/service")
    public ModelAndView service(){
        return new ModelAndView("/view/service");
    }

}
