package com.example.demo.service.login;

import com.example.demo.form.PasswordForm;
import com.example.demo.model.AppUser;
import com.example.demo.service.IService;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface IAppUserService extends UserDetailsService, IService<AppUser> {

    AppUser getCurrentUser();

    AppUser findByEmail(String email);

    List<AppUser> getAllUserByAppRole(String role);

    List<AppUser> findListAppUserByEmail(String email);

    String confirmToken(String token);

    String register(AppUser appUser, HttpServletRequest request);

    Boolean changePassword(PasswordForm passwordForm);

    Boolean forgottenPassword(PasswordForm passwordForm, HttpServletRequest request);

    String resetPassword(String token, PasswordForm passwordForm);
}
