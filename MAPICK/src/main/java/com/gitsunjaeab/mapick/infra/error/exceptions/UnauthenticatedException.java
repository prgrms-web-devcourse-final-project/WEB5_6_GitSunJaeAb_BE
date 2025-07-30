package com.gitsunjaeab.mapick.infra.error.exceptions;

import com.gitsunjaeab.mapick.infra.common.response.ResponseCode;
import lombok.Getter;

@Getter
public class UnauthenticatedException extends  RuntimeException {

    private final ResponseCode code;

    public UnauthenticatedException(ResponseCode code) {
        super(code.getMessage());
        this.code = code;
    }

}
