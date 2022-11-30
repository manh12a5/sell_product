package com.example.demo.controller;

import com.example.demo.model.login.AppUser;
import com.example.demo.service.cart.ICartService;
import com.example.demo.service.login.IAppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    IAppUserService appUserService;

    @Autowired
    ICartService cartService;

    private AppUser currentUser() {
        return appUserService.getCurrentUser();
    }

    @GetMapping("")
    public String showFormLogin() {
        return "login/login";
    }

    @GetMapping("/header")
    public String getLoginHeader() {
        AppUser appUser = currentUser();

        return "flagment/header";
    }

    @GetMapping("/register")
    public String showFormRegistration(Model model) {
        model.addAttribute("user", new AppUser());
        return "login/create";
    }

    @PostMapping("/register")
    public String createAccount(@ModelAttribute AppUser appUser, HttpServletRequest request) {
        appUserService.register(appUser, request);
        return "login/confirm";
    }

    @GetMapping("/register/confirm")
    public String confirmAccount(@RequestParam("token") String token) {
        appUserService.confirmToken(token);
        return "redirect:/login";
    }

}
