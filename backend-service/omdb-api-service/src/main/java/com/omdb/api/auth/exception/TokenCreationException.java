package com.omdb.api.auth.exception;

public class TokenCreationException extends RuntimeException {
    public TokenCreationException(String message) {
        super(message);
    }
}
