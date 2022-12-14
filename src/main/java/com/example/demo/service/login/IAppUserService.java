package com.example.demo.service.login;

import com.example.demo.model.AppUser;
import com.example.demo.service.IService;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface IAppUserService extends UserDetailsService, IService<AppUser> {
    AppUser getCurrentUser();
    AppUser findByEmail(String email);
    List<AppUser> findListAppUserByEmail(String email);
    String confirmToken(String token);
    String register(AppUser appUser, HttpServletRequest request);
}
