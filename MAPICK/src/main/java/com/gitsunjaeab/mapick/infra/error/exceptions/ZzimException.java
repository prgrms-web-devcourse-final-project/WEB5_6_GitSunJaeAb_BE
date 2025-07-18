package com.gitsunjaeab.mapick.infra.error.exceptions;

import com.gitsunjaeab.mapick.common.response.ResponseCode;

public class ZzimException extends CommonException {

    public ZzimException(ResponseCode code) {
        super(code);
    }

    public static ZzimException alreadyZzimed() {
        return new ZzimException(ResponseCode.ALREADY_ZZIMMED);
    }

    public static ZzimException notZzimedYet() {
        return new ZzimException(ResponseCode.NOT_ZZIMMED_YET);
    }
}