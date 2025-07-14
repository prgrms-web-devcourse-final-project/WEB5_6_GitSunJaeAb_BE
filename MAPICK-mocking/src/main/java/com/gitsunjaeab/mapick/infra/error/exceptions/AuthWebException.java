package com.gitsunjaeab.mapick.infra.error.exceptions;

import com.gitsunjaeab.mapick.common.response.ResponseCode;

public class AuthWebException extends CommonException {
    
    public AuthWebException(ResponseCode code) {
        super(code);
    }
    
    public AuthWebException(ResponseCode code, String redirect) {
        super(code, redirect);
    }
}
