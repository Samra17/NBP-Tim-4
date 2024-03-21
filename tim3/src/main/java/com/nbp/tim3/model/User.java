package com.nbp.tim3.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {
    int id;
    String firstName;
    String lastName;

    String email;

    String password;

    String username;

    String phoneNumber;

    Role role;

    Address address;
}
