package com.gitsunjaeab.mapick.infra.error;

import com.gitsunjaeab.mapick.api.roadmap.dto.bookmark.BookmarkCreateResponse;
import com.gitsunjaeab.mapick.api.roadmap.dto.bookmark.BookmarkDeleteResponse;
import com.gitsunjaeab.mapick.common.response.ApiResponse;
import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ErrorResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.infra.error.exceptions.CommonException;
import com.gitsunjaeab.mapick.infra.error.exceptions.DuplicatedBookmarkException;
import com.gitsunjaeab.mapick.infra.error.exceptions.ForbiddenException;
import com.gitsunjaeab.mapick.infra.error.exceptions.InvalidRoadmapTypeException;
import com.gitsunjaeab.mapick.infra.error.exceptions.UnauthenticatedException;
import com.gitsunjaeab.mapick.infra.error.exceptions.UnauthorizedAccessException;
import java.time.OffsetDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.gitsunjaeab.mapick")
@Slf4j
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

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("잘못된 요청 매개변수 오류: {}", e.getMessage());

        return ResponseEntity
            .status(ResponseCode.INVALID_INPUT.getStatus())
            .body(ApiResponse.of(ResponseCode.INVALID_INPUT, e.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse> handleDataIntegrityViolationException(
        DataIntegrityViolationException e) {
        log.error("DB 제약 조건 위반 오류 발생: {}", e.getMessage());

        // 중복 키 오류 메시지 커스터마이징
        String errorMessage = "DB 제약 조건에 위배되었습니다.";
        if (e.getMessage().contains("duplicate key")) {
            errorMessage = "중복된 데이터가 존재합니다. 다시 시도해주세요.";
        } else if (e.getMessage().contains("foreign key")) {
            errorMessage = "참조 무결성 제약 조건에 위배되었습니다.";
        }

        return ResponseEntity
            .status(ResponseCode.DB_CONSTRAINT_VIOLATION.getStatus())
            .body(ApiResponse.of(ResponseCode.DB_CONSTRAINT_VIOLATION, errorMessage));
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
    public ResponseEntity<BaseApiResponse> handleDuplicatedBookmark(
        DuplicatedBookmarkException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new BookmarkCreateResponse("4001", ex.getMessage(), OffsetDateTime.now()));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<BaseApiResponse> handleForbiddenFromBookMarkDelete(
        ForbiddenException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new BookmarkDeleteResponse("4001", ex.getMessage(), OffsetDateTime.now()));
    }

    @ExceptionHandler(InvalidRoadmapTypeException.class)
    public ResponseEntity<BaseApiResponse> handleInvalidType(InvalidRoadmapTypeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new BookmarkDeleteResponse(ResponseCode.INVALID_ROADMAP_TYPE.getCode(),
                ex.getMessage(), OffsetDateTime.now()));
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<BaseApiResponse> handleUnauthorized(UnauthorizedAccessException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
            new ErrorResponse(
                ResponseCode.FORBIDDEN.getCode(),
                ex.getMessage(),
                OffsetDateTime.now()
            )
        );
    }
}