package com.omdb.api.auth.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("user_app")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UserApp {

    @Id
    private Long id;
    private String username;
    @Column("password_hash")
    private String password;
    private String email;

    public UserApp(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

}
