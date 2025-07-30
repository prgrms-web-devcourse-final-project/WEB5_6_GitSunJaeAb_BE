package com.gitsunjaeab.mapick.application.domain.auth;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessTokenBlacklistRepository extends JpaRepository<AccessTokenBlacklist, String> {
    boolean existsByToken(String token);
}
