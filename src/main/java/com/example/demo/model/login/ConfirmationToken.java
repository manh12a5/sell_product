package com.example.demo.model.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ConfirmationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime createdTime;

    @Column(nullable = false)
    private LocalDateTime expiredTime;

    private LocalDateTime confirmedAt;

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private AppUser appUser;

    public ConfirmationToken(String token,
                             LocalDateTime createdTime,
                             LocalDateTime expiredTime) {
        this.token = token;
        this.createdTime = createdTime;
        this.expiredTime = expiredTime;
    }

    public ConfirmationToken(String token,
                             LocalDateTime createdTime,
                             LocalDateTime expiredTime,
                             AppUser appUser) {
        this.token = token;
        this.createdTime = createdTime;
        this.expiredTime = expiredTime;
        this.appUser = appUser;
    }
}
