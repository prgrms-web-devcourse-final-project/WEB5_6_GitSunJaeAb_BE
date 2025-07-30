package com.gitsunjaeab.mapick.infra.common.util;

import com.gitsunjaeab.mapick.infra.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.auth.Principal;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.infra.error.exceptions.UnauthenticatedException;

public class AuthUtil {

    public static Member getAuthenticatedMember(Principal principal) {
        if (principal == null || principal.getMember() == null) {
            throw new UnauthenticatedException(ResponseCode.UNAUTHORIZED);
        }
        return principal.getMember();
    }
}