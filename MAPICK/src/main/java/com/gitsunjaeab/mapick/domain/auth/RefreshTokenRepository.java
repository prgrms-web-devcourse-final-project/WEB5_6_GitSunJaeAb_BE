package com.gitsunjaeab.mapick.domain.auth;

import java.util.Optional;

import com.gitsunjaeab.mapick.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    Optional<RefreshToken> findByAccessTokenId(String id);

    long deleteByToken(String token);
    long deleteByAccessTokenId(String accessTokenId);

    void deleteByMember(Member member);
    void deleteByMemberId(Long memberId);
}