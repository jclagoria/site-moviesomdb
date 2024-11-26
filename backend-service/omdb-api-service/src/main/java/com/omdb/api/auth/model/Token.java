package com.omdb.api.auth.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;

@Table("tokens")
public class Token {

    @Id
    private Long id;
    private Long userId;
    private String token;
    private boolean isActive;

    public Token() {
    }

    public Token(Long userId, String token, boolean isActive) {
        this.userId = userId;
        this.token = token;
        this.isActive = isActive;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Token token1)) return false;
        return isActive == token1.isActive
                && Objects.equals(id, token1.id)
                && Objects.equals(userId, token1.userId)
                && Objects.equals(token, token1.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, token, isActive);
    }
}
