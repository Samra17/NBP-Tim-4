package com.nbp.tim3.auth.controller;

import com.nbp.tim3.auth.dto.AuthResponse;
import com.nbp.tim3.auth.dto.UserResponse;
import com.nbp.tim3.auth.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @Operation(description = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched all users",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class)) }),
            @ApiResponse(responseCode = "403", description = "Unauthorized access",
                    content = @Content)})
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping("/all")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(description = "Get all restaurant managers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched all users with restaurant manager role",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class)) }),
            @ApiResponse(responseCode = "403", description = "Unauthorized access",
                    content = @Content)})
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping("/managers")
    public ResponseEntity<List<UserResponse>> getAllManagers() {
        return ResponseEntity.ok(userService.getAllManagers());
    }

    @Operation(description = "Get all couriers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched all users with courier role",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class)) }),
            @ApiResponse(responseCode = "403", description = "Unauthorized access",
                    content = @Content)})
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping("/couriers")
    public ResponseEntity<List<UserResponse>> getAllCouriers() {
        return ResponseEntity.ok(userService.getAllCouriers());
    }

    /*
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }*/

    @Operation(description = "Get logged in user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched user",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class)) }),
            @ApiResponse(responseCode = "403", description = "Unauthorized access",
                    content = @Content)})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/current")
    public ResponseEntity<UserResponse> getLoggedInUser(
            @Parameter(description = "User username", required = false)
            @RequestHeader(value = "username", required = false) String username
    ) {
        return ResponseEntity.ok(userService.getLoggedInUser(username));
    }


}
