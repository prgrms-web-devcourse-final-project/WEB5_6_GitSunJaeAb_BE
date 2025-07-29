package com.gitsunjaeab.mapick.application.auth;

import com.gitsunjaeab.mapick.domain.auth.RefreshToken;
import com.gitsunjaeab.mapick.domain.auth.RefreshTokenRepository;
import java.util.Optional;
import java.util.UUID;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    // (삭제) 로그아웃 시, 관련 RefreshToken 삭제
    public void deleteByAccessTokenId(String id) {
        Optional<RefreshToken> optional = refreshTokenRepository.findByjti(id);
        optional.ifPresent(e -> refreshTokenRepository.deleteById(e.getId()));
    }

    // (갱신) RefreshToken 재발급 및 AccessToken과 연결
    public RefreshToken renewingToken(String oldAccessTokenId, String newAccessTokenId) {
        RefreshToken refreshToken = findByAccessTokenId(oldAccessTokenId); // 구 jti로 refresh token 객체 생성
        refreshToken.setRefreshToken(UUID.randomUUID().toString()); // 새 refresh token 저장
        refreshToken.setJti(newAccessTokenId); // 새 access token id 저장
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken findByAccessTokenId(String jti){
        return refreshTokenRepository.findByjti(jti)
                .orElseThrow(() -> new JwtException("❌ AccessToken에 대응되는 RefreshToken이 존재하지 않습니다."));
    }

}