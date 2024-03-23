package com.nbp.tim3.auth.controller;

import com.nbp.tim3.auth.dto.*;
import com.nbp.tim3.auth.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(path="/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {

        return new ResponseEntity<>(authenticationService.register(registerRequest,"CUSTOMER"), HttpStatus.CREATED);
    }

    @PostMapping("/user/update")
    public ResponseEntity<AuthResponse> updateUser(@Valid @RequestBody UserUpdateRequest userUpdateRequest) {

        return new ResponseEntity<>(authenticationService.updateUser(userUpdateRequest), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PostMapping("/register-admin")
    public ResponseEntity<AuthResponse> registerAdmin(@Valid @RequestBody RegisterRequest registerRequest) {

        return new ResponseEntity<>(authenticationService.register(registerRequest,"ADMINISTRATOR"), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PostMapping("/register-courier")
    public ResponseEntity<AuthResponse> registerCourier(@Valid @RequestBody RegisterRequest registerRequest) {

        return new ResponseEntity<>(authenticationService.register(registerRequest,"COURIER"), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PostMapping("/register-manager")
    public ResponseEntity<AuthResponse> registerRestaurantManager(@Valid @RequestBody RegisterRequest registerRequest) {

        return new ResponseEntity<>(authenticationService.register(registerRequest,"RESTAURANT_MANAGER"), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthCredentials credentials) {

        return ResponseEntity.ok(authenticationService.authenticate(credentials));

    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refresh(HttpServletRequest request, HttpServletResponse response) {
        try {
            return ResponseEntity.ok(authenticationService.refreshToken(request,response));
        } catch (IOException e) {
            logger.error("Failed to refresh token");
            return (ResponseEntity<AuthResponse>) ResponseEntity.badRequest();
        }
    }

    @PostMapping("/validate-token")
    public ResponseEntity<ValidationResponse> validateToken(HttpServletRequest request) {
        return ResponseEntity.ok(authenticationService.validateToken(request));
    }

    @GetMapping("/uuid-token/{uuid}")
    public ResponseEntity<String> getTokenFromUUID(@PathVariable String uuid) {
        return ResponseEntity.ok(authenticationService.getTokenFromUUID(uuid));
    }

}
