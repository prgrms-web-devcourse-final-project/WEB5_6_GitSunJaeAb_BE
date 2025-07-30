package com.gitsunjaeab.mapick.infra.error.exceptions;

import com.gitsunjaeab.mapick.infra.common.response.ResponseCode;
import lombok.Getter;

@Getter
public class DuplicatedBookmarkException extends RuntimeException {

    private final ResponseCode code;

    public DuplicatedBookmarkException(ResponseCode code) {
        super(code.getMessage());
        this.code = code;
    }
}
