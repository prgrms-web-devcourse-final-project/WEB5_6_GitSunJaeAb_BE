package com.gitsunjaeab.mapick.application.api.auth.dto.internal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SocialUserInfo {
    private String name;
    private String email;
    private String picture;
}