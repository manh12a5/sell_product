package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.login.IAppUserService;
import com.example.demo.service.product.IProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ajax")
@Slf4j
public class AjaxController {

    @Autowired
    private IAppUserService appUserService;

    @Autowired
    private IProductService productService;

    @GetMapping("/findProducts")
    public List<Product> findProducts(@RequestParam("nameProduct") String nameProduct) {
        log.info("[findProducts]: {}", nameProduct);
        return productService.findListProductByName(nameProduct);
    }

}
