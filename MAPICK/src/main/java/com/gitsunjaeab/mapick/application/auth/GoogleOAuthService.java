package com.gitsunjaeab.mapick.application.auth;

import com.gitsunjaeab.mapick.api.member.dto.SocialUserInfo;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class GoogleOAuthService {

    public SocialUserInfo getUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                "https://www.googleapis.com/oauth2/v3/userinfo",
                HttpMethod.GET,
                entity,
                Map.class
        );

        Map<String, Object> body = response.getBody();
        log.info("Google 응답 정보: {}", body);

        return new SocialUserInfo(
                (String) body.get("name"),
                (String) body.get("email"),
                (String) body.get("picture")
        );
    }
}