package com.omdb.api.exception;

import com.omdb.api.auth.exception.InvalidCredentialsException;
import com.omdb.api.auth.exception.TokenCreationException;
import com.omdb.api.auth.exception.UserNotFoundException;
import com.omdb.api.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public Mono<ResponseEntity<ApiResponse>> handleRuntimeException(final RuntimeException exception) {
        ApiResponse errorResponse = new ApiResponse(exception.getMessage(),
                HttpStatus.BAD_REQUEST.value());
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ApiResponse>> handleGenericException(final Exception exception) {
        ApiResponse errorResponse = new ApiResponse("Internal server error",
                HttpStatus.INTERNAL_SERVER_ERROR.value());
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public Mono<ResponseEntity<ApiResponse>> handleUserNotFound(final UserNotFoundException exception) {
        ApiResponse errorResponse = new ApiResponse(exception.getMessage(), HttpStatus.NOT_FOUND.value());
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(errorResponse));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public Mono<ResponseEntity<ApiResponse>> handleInvalidCredentials(final InvalidCredentialsException exception) {
        ApiResponse errorResponse = new ApiResponse(exception.getMessage(), HttpStatus.UNAUTHORIZED.value());
        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(errorResponse));
    }

    @ExceptionHandler(TokenCreationException.class)
    public Mono<ResponseEntity<ApiResponse>> handleTokenCreation(final TokenCreationException ex) {
        ApiResponse errorResponse = new ApiResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
    }

}
