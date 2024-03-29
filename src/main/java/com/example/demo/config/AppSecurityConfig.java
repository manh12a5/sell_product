package com.example.demo.config;

import com.example.demo.service.oAuth2.CustomOAuth2Service;
import com.example.demo.service.login.IAppUserService;
import com.example.demo.service.oAuth2.OAuth2LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AppSecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    private final static String[] URL_PERMIT_ALL = {
            "/", "/categories/**", "/products/**", "/cart/**",
            "/checkout/**", "/ajax/**", "/wishlist/**", "/oauth2/**"
    };

    private final static String[] URL_PERMIT_ADMIN = {
            "/admin/**", "/products/manager", "/products/create",
            "/products/edit/{id}", "/products/delete/{id}"
    };

    @Autowired
    private IAppUserService appUserService;

    @Autowired
    private CustomizeSuccessHandle customizeSuccessHandle;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private CustomOAuth2Service customOAuth2Service;

    @Autowired
    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable() //It use for postman or other applications to get API
                .authorizeRequests()
                .antMatchers(URL_PERMIT_ALL).permitAll()
                .antMatchers(URL_PERMIT_ADMIN).hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.POST ,"/products/detail/{id}")
                .authenticated()
                .and()
                .formLogin().successHandler(customizeSuccessHandle).loginPage("/login").permitAll()
                .and()
                .oauth2Login().loginPage("/login").userInfoEndpoint().userService(customOAuth2Service)
                    .and().successHandler(oAuth2LoginSuccessHandler)
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).and()
                .exceptionHandling().accessDeniedPage("/403");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
        daoAuthenticationProvider.setUserDetailsService(appUserService);
        return daoAuthenticationProvider;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/403").setViewName("login/403denied");
    }

}
