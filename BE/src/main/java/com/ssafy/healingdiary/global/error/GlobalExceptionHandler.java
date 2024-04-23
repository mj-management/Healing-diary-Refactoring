package com.ssafy.healingdiary.global.error;

import javax.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //custom error
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(final CustomException e) {
        log.error("handleCustomException: {}", e.getErrorCode());
        e.printStackTrace();
        return ResponseEntity
            .status(e.getErrorCode().getStatus().value())
            .body(new ErrorResponse(e.getErrorCode()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleEntityNotFoundException(final EntityNotFoundException e) {
        log.error("handleEntityNotFoundException: {}", e.getMessage());
        return ResponseEntity
            .status(ErrorCode.ENTITY_NOT_FOUND.getStatus().value())
            .body(new ErrorResponse(ErrorCode.ENTITY_NOT_FOUND));
    }

    //405 error
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException e) {
        log.error("handleHttpRequestMethodNotSupportedException: {}", e.getMessage());
        return ResponseEntity
            .status(ErrorCode.METHOD_NOT_ALLOWED.getStatus().value())
            .body(new ErrorResponse(ErrorCode.METHOD_NOT_ALLOWED));
    }
    @ExceptionHandler(RestClientException.class)
    protected ResponseEntity<ErrorResponse> handleRestClientException(final Exception e) {
        log.error("handleRestClientException: {}", e.getMessage());
        return ResponseEntity
                .status(ErrorCode.TOKEN_NOT_VALID.getStatus().value())
                .body(new ErrorResponse(ErrorCode.TOKEN_NOT_VALID));
    }
    //500 error
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(final Exception e) {
        e.printStackTrace();
        log.error("handleException: {}", e.getMessage());
        return ResponseEntity
            .status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus().value())
            .body(new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR));
    }


}
