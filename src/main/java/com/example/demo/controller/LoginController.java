package com.example.demo.controller;

import com.example.demo.form.PasswordForm;
import com.example.demo.model.AppUser;
import com.example.demo.model.ConfirmationToken;
import com.example.demo.service.cart.ICartService;
import com.example.demo.service.confirmationToken.ConfirmationTokenService;
import com.example.demo.service.login.IAppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.UUID;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private IAppUserService appUserService;

    @Autowired
    private ICartService cartService;

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private AppUser currentUser() {
        return appUserService.getCurrentUser();
    }

    @GetMapping("")
    public String showFormLogin() {
        return "login/login";
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

    @GetMapping("/changePassword")
    public String getChangePassword(Model model) {
        model.addAttribute("passwordForm", new PasswordForm());
        return "login/change-password";
    }

    @PostMapping("/changePassword")
    public String changePassword(@ModelAttribute PasswordForm passwordForm, Model model) {
        Boolean checkChangePassword = appUserService.changePassword(passwordForm);
        if (checkChangePassword) {
            model.addAttribute("messages", "Changed password error");
            return "login/change-password";
        }

        model.addAttribute("messages", "Changed password successfully");
        return "login/login";
    }

    @GetMapping("/forgottenPassword")
    public String getForgottenPassword(Model model) {
        model.addAttribute("passwordForm", new PasswordForm());
        model.addAttribute("sentEmail", false);
        return "login/forgotten-password";
    }

    @PostMapping("/forgottenPassword")
    public String forgottenPassword(@ModelAttribute PasswordForm passwordForm, Model model, HttpServletRequest request) {
        Boolean checkForgotten = appUserService.forgottenPassword(passwordForm, request);
        model.addAttribute("sentEmail", checkForgotten);
        model.addAttribute("error", checkForgotten);

        return "login/forgotten-password";
    }

    @GetMapping("/resetPassword")
    public String getResetPassword(@RequestParam String token, Model model) {
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token);
        LocalDateTime presentTime = LocalDateTime.now();
        if (confirmationToken != null
                && confirmationToken.getExpiredTime().isAfter(presentTime)) {
            model.addAttribute("username", confirmationToken.getAppUser().getEmail());
            model.addAttribute("passwordForm", new PasswordForm());

            return "login/reset-password";
        }

        return "login/403denied";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@ModelAttribute PasswordForm passwordForm, Model model) {
        AppUser appUser = appUserService.findByEmail(passwordForm.getUsername());

        if (appUser != null) {
            appUser.setPassword(passwordEncoder.encode(passwordForm.getNewPassword()));
            appUserService.save(appUser);

            model.addAttribute("success", "Change password success");
            return "/login/login";
        }

        return "login/403denied";
    }

}
