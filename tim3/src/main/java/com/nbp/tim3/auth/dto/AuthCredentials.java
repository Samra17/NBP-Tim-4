package com.nbp.tim3.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthCredentials {
    @NotNull(message = "Username is required!")
    private String username;
    @NotNull(message = "Password is required!")
    private String password;
}
