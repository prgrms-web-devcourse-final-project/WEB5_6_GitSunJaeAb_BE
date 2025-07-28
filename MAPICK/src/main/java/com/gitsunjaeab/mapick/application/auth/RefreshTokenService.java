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
    public RefreshToken renewingToken(String oldAccessTokenId, String newAccessTokenId) {
        RefreshToken refreshToken = findByAccessTokenId(oldAccessTokenId);
        refreshToken.setToken(UUID.randomUUID().toString()); // 새 refresh token 저장
        refreshToken.setAccessTokenId(newAccessTokenId); // 새 access token 저장
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    // (조회) AccessToken에 대응되는 RefreshToken을 찾는 데 사용
    public RefreshToken findByAccessTokenId(String id){
        return refreshTokenRepository.findByAccessTokenId(id).orElse(null);
    }
}