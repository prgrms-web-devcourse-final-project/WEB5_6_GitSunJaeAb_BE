package com.gitsunjaeab.mapick.infra.error.exceptions;

import com.gitsunjaeab.mapick.common.response.ResponseCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonException extends RuntimeException {
    
    private final ResponseCode code;
    private String redirect = "/";
    
    public CommonException(ResponseCode code) {
        super(code.getMessage());
        this.code = code;
    }
    
    public CommonException(ResponseCode code, String redirect) {
        this.code = code;
        this.redirect = redirect;
    }

    public String redirect(){return redirect; }
    public ResponseCode code() {
        return code;
    }
}
