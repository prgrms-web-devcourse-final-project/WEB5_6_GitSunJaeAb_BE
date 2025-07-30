package com.gitsunjaeab.mapick.application.domain.auth;

import java.util.Optional;

import com.gitsunjaeab.mapick.application.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    Optional<RefreshToken> findByjti(String jti);

    long deleteByjti(String jti);

    void deleteByMember(Member member);

}