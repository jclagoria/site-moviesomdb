package com.omdb.api.auth.dto;

public record RegisterRequest(String username, String password, String email) {
}
