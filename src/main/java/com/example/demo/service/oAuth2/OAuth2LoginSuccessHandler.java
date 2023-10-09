package com.example.demo.service.oAuth2;

import com.example.demo.model.AppUser;
import com.example.demo.model.CustomOAuth2;
import com.example.demo.object.ProviderEnum;
import com.example.demo.service.login.IAppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private IAppUserService appUserService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        CustomOAuth2 customOAuth2 = (CustomOAuth2) authentication.getPrincipal();

        AppUser appUser = new AppUser();
        appUser.setEmail(customOAuth2.getEmail());
        appUser.setAuthProvider(ProviderEnum.FACEBOOK);
        appUser.setEnabled(true);
        appUser.setCreateOn(Timestamp.valueOf(LocalDateTime.now()));

        appUserService.register(appUser, request);

        super.onAuthenticationSuccess(request, response, authentication);
    }

}
