package com.example.demo.repository;

import com.example.demo.model.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    Optional<ConfirmationToken> findByAppUserId(Long id);
    Optional<ConfirmationToken> findByToken(String token);
}
