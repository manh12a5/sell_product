package com.example.demo.model;

import com.example.demo.object.ProviderEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AppUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Date birthday;
    private String phoneNumber;
    private String address;

    @Enumerated(EnumType.STRING)
    private AppRole appRole;
    private Boolean locked = false;
    private Boolean enabled = false;

    @Column(name = "created_on", nullable = true, insertable = false, updatable = false)
    private Timestamp createOn;

    @Enumerated(EnumType.STRING)
    private ProviderEnum authProvider;

    @JsonIgnore
    @OneToMany(mappedBy = "appUserWishListId.appUser", fetch = FetchType.LAZY)
    private Collection<AppUserWishList> appUserWishListCollection;

    //Constructor without ID
    public AppUser(String firstName,
                   String lastName,
                   String password,
                   String email,
                   AppRole appRole) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.appRole = appRole;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(appRole.name());
        return Collections.singletonList(simpleGrantedAuthority);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

}