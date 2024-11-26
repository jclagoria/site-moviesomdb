package com.omdb.api.auth.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;

@Table("user_app")
public class UserApp {

    @Id
    private Long id;
    private String username;
    @Column("password_hash")
    private String passwordHash;
    private String email;

    public UserApp() {}

    public UserApp(String username, String password, String email) {
        this.username = username;
        this.passwordHash = password;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserApp userApp)) return false;
        return Objects.equals(id, userApp.id)
                && Objects.equals(username, userApp.username)
                && Objects.equals(passwordHash, userApp.passwordHash)
                && Objects.equals(email, userApp.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, passwordHash, email);
    }
}
