package com.gitsunjaeab.mapick.infra.error;

import com.gitsunjaeab.mapick.api.roadmap.dto.bookmark.BookmarkCreateResponse;
import com.gitsunjaeab.mapick.api.roadmap.dto.bookmark.BookmarkDeleteResponse;
import com.gitsunjaeab.mapick.common.response.ApiResponse;
import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.infra.error.exceptions.CommonException;
import com.gitsunjaeab.mapick.infra.error.exceptions.DuplicatedBookmarkException;
import com.gitsunjaeab.mapick.infra.error.exceptions.ForbiddenException;
import com.gitsunjaeab.mapick.infra.error.exceptions.UnauthenticatedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice(basePackages = "com.gitsunjaeab.mapick")
public class RestExceptionAdvice {

    @ExceptionHandler(CommonException.class)
    public ResponseEntity<ApiResponse> handleCommonException(CommonException e) {
        e.printStackTrace();
        return ResponseEntity.ok(ApiResponse.of(e.code()));
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse handleEtcException(Exception e) {
        e.printStackTrace(); // 디버깅용 로그
        return ApiResponse.of(ResponseCode.INTERNAL_ERROR);
    }

    @ExceptionHandler(UnauthenticatedException.class)
    public ResponseEntity<ApiResponse> handleUnauthenticatedException(UnauthenticatedException e) {
        return ResponseEntity
                .status(e.getCode().getStatus())
                .body(ApiResponse.of(e.getCode()));
    }

    @ExceptionHandler(DuplicatedBookmarkException.class)
    public ResponseEntity<BaseApiResponse> handleDuplicatedBookmark(DuplicatedBookmarkException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new BookmarkCreateResponse("4001", ex.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<BaseApiResponse> handleForbiddenFromBookMarkDelete(ForbiddenException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new BookmarkDeleteResponse("4001", ex.getMessage(), LocalDateTime.now()));
    }
}