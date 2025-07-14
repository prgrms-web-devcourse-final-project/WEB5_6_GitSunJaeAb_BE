package com.gitsunjaeab.mapick.domain.auth;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    Optional<RefreshToken> findByAccessTokenId(String id);

    long deleteByToken(String token);
}