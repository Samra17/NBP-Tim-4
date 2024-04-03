package com.nbp.tim3.model;

import com.nbp.tim3.auth.dto.RegisterRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    int id;
    String firstName;
    String lastName;

    String email;

    String password;

    String username;

    String phoneNumber;

    Role role;

    Address address;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_" + role.getName()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User(RegisterRequest request) {
        firstName = request.getFirstname();
        lastName = request.getLastname();
        email = request.getEmail();
        username = request.getUsername();
        phoneNumber = request.getPhoneNumber();
    }
}
