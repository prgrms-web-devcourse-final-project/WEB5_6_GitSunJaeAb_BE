package com.gitsunjaeab.mapick.infra.error;

import com.gitsunjaeab.mapick.common.response.ApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.infra.error.exceptions.CommonException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.gitsunjaeab.mapick")
public class RestExceptionAdvice {

    @ExceptionHandler(CommonException.class)
    public ResponseEntity<ApiResponse> handleCommonException(CommonException e) {
        e.printStackTrace();
        return ResponseEntity.ok(ApiResponse.of(e.code()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ApiResponse handleAccessDenied(AccessDeniedException e) {
        e.printStackTrace();
        return ApiResponse.of(ResponseCode.FORBIDDEN); // 403
    }

    @ExceptionHandler(AuthenticationException.class)
    public ApiResponse handleAuthentication(AuthenticationException e) {
        e.printStackTrace();
        return ApiResponse.of(ResponseCode.UNAUTHORIZED); // 401
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse handleEtcException(Exception e) {
        e.printStackTrace(); // 디버깅용 로그
        return ApiResponse.of(ResponseCode.INTERNAL_ERROR);
    }
}