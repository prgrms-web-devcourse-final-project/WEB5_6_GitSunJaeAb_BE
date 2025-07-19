package com.gitsunjaeab.mapick.infra.auth;

import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.auth.Principal;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.infra.error.exceptions.UnauthenticatedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthHelper {

    public Member getCurrentMember() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof Principal principal)) {
            throw new UnauthenticatedException(ResponseCode.UNAUTHORIZED);
        }
        return principal.getMember();
    }

    public Long getCurrentMemberId() {
        return getCurrentMember().getId();
    }
}