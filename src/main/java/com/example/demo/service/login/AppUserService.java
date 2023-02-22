package com.example.demo.service.login;

import com.example.demo.email.EmailSender;
import com.example.demo.form.PasswordForm;
import com.example.demo.model.AppRole;
import com.example.demo.model.AppUser;
import com.example.demo.model.Cart;
import com.example.demo.model.ConfirmationToken;
import com.example.demo.repository.AppUserRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ConfirmationTokenRepository;
import com.example.demo.service.confirmationToken.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AppUserService implements IAppUserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private CartRepository cartRepository;

    private final EntityManager entityManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email).orElse(null);
    }

    @Override
    public AppUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String email = authentication.getName();
            return appUserRepository.findByEmail(email).orElse(null);
        }
        return null;
    }

    @Override
    public List<AppUser> findAll() {
        return appUserRepository.findAll();
    }

    @Override
    public AppUser findById(Long id) {
        return appUserRepository.findById(id).orElse(null);
    }

    @Override
    public AppUser findByEmail(String email) {
        return appUserRepository.findByEmail(email).orElse(null);
    }

    @Override
    public AppUser save(AppUser appUser) {
        return appUserRepository.save(appUser);
    }

    @Override
    public void remove(Long id) {
        appUserRepository.deleteById(id);
    }

    @Override
    public List<AppUser> getAllUserByAppRole(String role) {
        return appUserRepository.getAllUsersByAppRole(role);
    }

    @Override
    public List<AppUser> findListAppUserByEmail(String email) {
        String sql = "SELECT * FROM app_user WHERE email LIKE :email ";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("email", "%" + email);

        List<AppUser> result = query.getResultList();

        return result;
    }

    @Transactional
    @Override
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token);

        if (confirmationToken.getConfirmedAt() != null) {
            return "Email already confirm";
        }

        LocalDateTime presentTime = LocalDateTime.now();
        LocalDateTime expiredTime = confirmationToken.getExpiredTime();

        if (expiredTime.isBefore(presentTime)) {
            return "Token expired";
        }

        confirmationToken.setConfirmedAt(presentTime);

        AppUser appUser = appUserRepository.findByEmail(confirmationToken.getAppUser().getEmail())
                .orElse(null);
        if (appUser != null) {
            appUser.setEnabled(true);
            appUserRepository.save(appUser);
        }

        return "redirect:/login";
    }

    @Transactional
    @Override
    public String register(AppUser appUser, HttpServletRequest request) {
        AppUser user = appUserRepository.findByEmail(appUser.getEmail())
                .orElse(null);
        if (user == null) {
            String encodePassword = bCryptPasswordEncoder.encode(appUser.getPassword());
            appUser.setPassword(encodePassword);
            appUser.setAppRole(AppRole.USER);

            appUserRepository.save(appUser);

            AppUser appUserAfterSave = appUserRepository.findByEmail(appUser.getEmail())
                    .orElse(null);
            Cart cart = new Cart();
            cart.setAppUser(appUserAfterSave);
            cartRepository.save(cart);
        } else {
            if (user.getEnabled()) {
                return "Email already taken";
            }
        }

        ConfirmationToken confirmationToken = confirmationTokenRepository.findByAppUserId(appUser.getId())
                .orElse(new ConfirmationToken());

        String token = UUID.randomUUID().toString();
        if (confirmationToken.getAppUser() == null) {
            confirmationToken.setAppUser(appUser);
        }
        confirmationToken.setToken(token);
        confirmationToken.setCreatedTime(LocalDateTime.now());
        confirmationToken.setExpiredTime(LocalDateTime.now().plusMinutes(15));

        confirmationTokenRepository.save(confirmationToken);

        String url = applicationUrl("register", token, request);
        Map<String, Object> templateAttributes = new LinkedHashMap<>();
        templateAttributes.put("name", appUser.getLastName());
        templateAttributes.put("url", url);

        emailSender.send(appUser.getEmail(), templateAttributes,
                "email/confirmRegistration", "Confirm your email");

        log.info("Confirmation Token is: {}", token);
        return "Saved user";
    }

    private String applicationUrl(String type, String token, HttpServletRequest request) {
        String url = "http://"
                + request.getServerName() //localhost
                + ":"
                + request.getServerPort() //Port
//                + request.getRequestURI();
                + "/login"
                + request.getContextPath();
        url += "/" + type + "?token=" + token;

        return url;
    }

    @Override
    public Boolean changePassword(PasswordForm passwordForm) {
        AppUser appUser = this.getCurrentUser();
        if (appUser != null
                && passwordEncoder.matches(passwordForm.getOldPassword(), appUser.getPassword())) {
            appUser.setPassword(passwordEncoder.encode(passwordForm.getNewPassword()));
            appUserRepository.save(appUser);
            return true;
        }

        return false;
    }

    @Override
    public Boolean forgottenPassword(PasswordForm passwordForm, HttpServletRequest request) {
        AppUser appUser = appUserRepository.findByEmail(passwordForm.getUsername()).orElse(null);

        if (appUser != null) {
            String token = UUID.randomUUID().toString();
            ConfirmationToken confirmationToken
                    = confirmationTokenService.getConfirmationTokenByUserId(appUser.getId());
            confirmationToken.setToken(token);
            confirmationToken.setCreatedTime(LocalDateTime.now());
            confirmationToken.setExpiredTime(LocalDateTime.now().plusMinutes(15));

            confirmationTokenRepository.save(confirmationToken);

            String url = applicationUrl("resetPassword", token, request);
            Map<String, Object> templateAttributes = new LinkedHashMap<>();
            templateAttributes.put("name", appUser.getLastName());
            templateAttributes.put("url", url);

            emailSender.send(appUser.getEmail(), templateAttributes,
                    "email/resetPassword", "Reset your password");

            log.info("Confirmation Token is: {}", token);

            return true;
        }

        return false;
    }

    @Override
    public String resetPassword(String token, PasswordForm passwordForm) {
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token);

        LocalDateTime presentTime = LocalDateTime.now();
        LocalDateTime expiredTime = confirmationToken.getExpiredTime();
        if (expiredTime.isBefore(presentTime)) {
            return "Token expired";
        }
        confirmationToken.setConfirmedAt(presentTime);

        AppUser appUser = appUserRepository.findByEmail(confirmationToken.getAppUser().getEmail())
                .orElse(null);
        if (appUser != null) {
            appUser.setPassword(passwordEncoder.encode(passwordForm.getNewPassword()));
            appUserRepository.save(appUser);
        }

        return "redirect:/login";
    }

}
