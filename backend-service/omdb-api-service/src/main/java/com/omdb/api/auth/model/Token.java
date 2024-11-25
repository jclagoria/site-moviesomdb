package com.omdb.api.auth.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("tokens")
@Data
public class Token {

    @Id
    private Long id;
    private Long userId;
    private String token;
    private boolean isActive;
}
