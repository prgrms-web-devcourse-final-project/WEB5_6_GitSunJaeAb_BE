package com.gitsunjaeab.mapick.infra.error.exceptions;

import com.gitsunjaeab.mapick.infra.common.response.ResponseCode;

public class AuthApiException extends CommonException{
    
    public AuthApiException(ResponseCode code) {
        super(code);
    }
}
