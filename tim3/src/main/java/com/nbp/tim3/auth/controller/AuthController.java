package com.nbp.tim3.auth.controller;

import com.nbp.tim3.auth.dto.*;
import com.nbp.tim3.auth.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
import java.util.HashMap;

@RestController
@RequestMapping(path="/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }



    @Operation(description = "Register a new customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created a new customer user",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized access",
                    content = @Content)})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Parameter(description = "Request with user information", required = true)
            @Valid @RequestBody RegisterRequest registerRequest) {

        return new ResponseEntity<>(authenticationService.register(registerRequest,"CUSTOMER"), HttpStatus.CREATED);
    }



    //dodati provjeru da li korisnik koji upućuje zahtjev ima isti id kao onaj koji se ažurira
    @Operation(description = "Update user information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated user information",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class)) }),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized access",
                    content = @Content)})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/user/update")
    public ResponseEntity<AuthResponse> updateUser(
            @Parameter(description = "Request with user information", required = true)
            @Valid @RequestBody UserUpdateRequest userUpdateRequest) {

        return new ResponseEntity<>(authenticationService.updateUser(userUpdateRequest), HttpStatus.OK);
    }

    @Operation(description = "Register a new admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created a new admin user",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized access",
                    content = @Content)})
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register-admin")
    public ResponseEntity<AuthResponse> registerAdmin(
            @Parameter(description = "Request with user information", required = true)
            @Valid @RequestBody RegisterRequest registerRequest) {

        return new ResponseEntity<>(authenticationService.register(registerRequest,"ADMINISTRATOR"), HttpStatus.CREATED);
    }

    @Operation(description = "Register a new courier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created a new courier user",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized access",
                    content = @Content)})
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register-courier")
    public ResponseEntity<AuthResponse> registerCourier(
            @Parameter(description = "Request with user information", required = true)
            @Valid @RequestBody RegisterRequest registerRequest) {

        return new ResponseEntity<>(authenticationService.register(registerRequest,"COURIER"), HttpStatus.CREATED);
    }

    @Operation(description = "Register a new restaurant manager")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created a new restaurant manager user",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized access",
                    content = @Content)})
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register-manager")
    public ResponseEntity<AuthResponse> registerRestaurantManager(
            @Parameter(description = "Request with user information", required = true)
            @Valid @RequestBody RegisterRequest registerRequest) {

        return new ResponseEntity<>(authenticationService.register(registerRequest,"RESTAURANT_MANAGER"), HttpStatus.CREATED);
    }

    @Operation(description = "Login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully logged in",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class)) }),
            @ApiResponse(responseCode = "403", description = "Invalid credentials",
                    content = @Content)})
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Parameter(description = "Login credentials", required = true)
            @Valid @RequestBody AuthCredentials credentials) {

        return ResponseEntity.ok(authenticationService.authenticate(credentials));

    }

    @Operation(description = "Generate new access token based on refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully generated new token",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class)) }),
            @ApiResponse(responseCode = "403", description = "Invalid token",
                    content = @Content)})
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refresh(HttpServletRequest request, HttpServletResponse response) {
        try {
            return ResponseEntity.ok(authenticationService.refreshToken(request,response));
        } catch (IOException e) {
            logger.error("Failed to refresh token");
            return (ResponseEntity<AuthResponse>) ResponseEntity.badRequest();
        }
    }

    @Operation(description = "Validate access token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully validated token",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class)) }),
            @ApiResponse(responseCode = "403", description = "Invalid token",
                    content = @Content)})
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/validate-token")
    public ResponseEntity<ValidationResponse> validateToken(HttpServletRequest request) {
        return ResponseEntity.ok(authenticationService.validateToken(request));
    }


}
