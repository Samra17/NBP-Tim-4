package com.nbp.tim3.auth.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

    private Integer id;
    @NotNull(message = "Firstname must be specified!")
    private String firstname;

    @NotNull(message = "Lastname must be specified!")
    private String lastname;

    @NotNull(message = "Username must be specified!")
    private String username;

    @NotNull(message = "Email address must be specified!")
    private String email;

    private String password;


    private String phoneNumber;


    private String address;


    @Pattern(regexp ="^((-)?[0-9]?[0-9]\\.\\d+,(\\s)*(-)?[1]?[0-9]?[0-9]\\.\\d+)",message = "Map coordinates must represent latitude and longitude values!")
    private String mapCoordinates;
}
