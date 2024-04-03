package com.nbp.tim3.model;

import com.nbp.tim3.auth.enums.TokenType;
import com.nbp.tim3.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    private Integer id;

    private String token;

    private boolean expired;

    private boolean revoked;

    private User user;


    public Token(String token, boolean expired, boolean revoked) {
        this.token = token;
        this.expired = expired;
        this.revoked = revoked;
    }

}
