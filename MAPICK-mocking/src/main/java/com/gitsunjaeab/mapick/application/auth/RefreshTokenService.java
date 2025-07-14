package com.gitsunjaeab.mapick.application.auth;

import com.gitsunjaeab.mapick.domain.auth.RefreshToken;
import com.gitsunjaeab.mapick.domain.auth.RefreshTokenRepository;
import java.util.Optional;
import java.util.UUID;
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
        Optional<RefreshToken> optional = refreshTokenRepository.findByAccessTokenId(id);
        optional.ifPresent(e -> refreshTokenRepository.deleteById(e.getId()));
    }

    // (갱신) RefreshToken 재발급 및 AccessToken과 연결
    public RefreshToken renewingToken(String id, String newTokenId) {
        RefreshToken refreshToken = findByAccessTokenId(id);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setAccessTokenId(newTokenId);
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    // (조회) AccessToken에 대응되는 RefreshToken을 찾는 데 사용
    public RefreshToken findByAccessTokenId(String id){
        return refreshTokenRepository.findByAccessTokenId(id).orElse(null);
    }
}