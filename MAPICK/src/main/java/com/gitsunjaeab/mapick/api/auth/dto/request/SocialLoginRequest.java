package com.gitsunjaeab.mapick.api.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SocialLoginRequest {
    private String provider;
    private String token; // access token 또는 id_token todo id_token을 처리 할 수 있도록 바꿔야함
}