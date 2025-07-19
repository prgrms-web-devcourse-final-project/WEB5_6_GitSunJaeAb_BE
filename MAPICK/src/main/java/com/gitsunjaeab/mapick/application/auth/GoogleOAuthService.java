package com.gitsunjaeab.mapick.application.auth;

import com.gitsunjaeab.mapick.api.auth.dto.SocialUserInfo;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Slf4j
public class GoogleOAuthService {

    public SocialUserInfo getUserInfo(String idTokenString) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    JacksonFactory.getDefaultInstance()
            )
                    .setAudience(Collections.singletonList("Google OAuth Client ID")) // aud 검증
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null) {
                throw new RuntimeException("ID Token 검증 실패: null");
            }

            GoogleIdToken.Payload payload = idToken.getPayload();

            return new SocialUserInfo(
                    (String) payload.get("name"),
                    payload.getEmail(),
                    (String) payload.get("picture")
            );

        } catch (Exception e) {
            throw new RuntimeException("Google ID Token 검증 중 오류 발생: " + e.getMessage(), e);
        }
    }
}