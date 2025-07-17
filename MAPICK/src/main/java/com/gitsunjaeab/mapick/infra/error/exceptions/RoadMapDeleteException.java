package com.gitsunjaeab.mapick.infra.error.exceptions;

import com.gitsunjaeab.mapick.common.response.ResponseCode;
import lombok.Getter;

@Getter
public class RoadMapDeleteException extends  RuntimeException {

    private final ResponseCode code;

    public RoadMapDeleteException(ResponseCode code) {
        super(code.getMessage());
        this.code = code;
    }

}
