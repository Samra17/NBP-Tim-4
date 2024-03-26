package com.nbp.tim3.model;

import com.nbp.tim3.auth.enums.TokenType;
import com.nbp.tim3.model.User;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class Token {
    private Integer id;

    private String token;

    private TokenType tokenType;

    private boolean expired;

    private boolean revoked;

    private User user;
    public Token() {
    }

    public Token(Integer id, String token, TokenType tokenType, boolean expired, boolean revoked, User user) {
        this.id = id;
        this.token = token;
        this.tokenType = tokenType;
        this.expired = expired;
        this.revoked = revoked;
        this.user = user;
    }

    public Token(String token, TokenType tokenType, boolean expired, boolean revoked, User user) {
        this.token = token;
        this.tokenType = tokenType;
        this.expired = expired;
        this.revoked = revoked;
        this.user = user;
    }


}
