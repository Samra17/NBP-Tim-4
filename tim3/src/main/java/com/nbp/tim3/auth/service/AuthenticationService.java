package com.nbp.tim3.auth.service;

import com.nbp.tim3.auth.dto.*;
import com.nbp.tim3.model.Address;
import com.nbp.tim3.model.Token;
import com.nbp.tim3.model.User;
import com.nbp.tim3.repository.RoleRepository;
import com.nbp.tim3.repository.TokenRepository;
import com.nbp.tim3.repository.UserRepository;
import com.nbp.tim3.util.exception.InvalidRequestException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final TokenRepository tokenRepository;

    @Autowired
    private final RoleRepository roleRepository;


    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;


    public AuthenticationService(UserRepository userRepository, TokenRepository tokenRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }


    public User getUserByUsername(String username) {
        var user = userRepository.getByUsername(username);

        return  user;
    }

    public AuthResponse register(RegisterRequest registerRequest, String roleName) {

        var role = roleRepository.getByName(roleName);

        if(role == null)
            throw  new InvalidRequestException(String.format("Role with name %s does not exist!",roleName));

        var user = new User(registerRequest);
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(role);

        Address address = null;

        if(registerRequest.getAddress() != null && registerRequest.getMapCoordinates() != null) {
            address = new Address();
            address.setStreet(registerRequest.getAddress());
            address.setMunicipality("Sarajevo");
            address.setMapCoordinates(registerRequest.getMapCoordinates());
        }

        user.setAddress(address);

        userRepository.addUser(user, address);

        Map<String,Object> claimMap= new HashMap<>();
        claimMap.put("Role",roleName);

        var jwtToken = jwtService.generateToken(claimMap,user);
        var refreshToken = jwtService.generateRefreshToken(claimMap, user);
        saveToken(user, jwtToken);
        return  new AuthResponse(jwtToken,refreshToken,new UserResponse(user));


    }



    private void saveToken(User savedUser, String jwtToken) {
        var token = new Token(jwtToken,false,false);
        token.setUser(savedUser);
        tokenRepository.addToken(token);


    }

    public AuthResponse authenticate(AuthCredentials authRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequest.getUsername(),
                authRequest.getPassword()));
        var user = userRepository.getByUsername(authRequest.getUsername());

        if(user == null)
            throw new EntityNotFoundException("User does not exist!");

        var role = user.getRole();
        Map<String,Object> claimMap= new HashMap<>();
        claimMap.put("Role",role.getName());

        var jwtToken = jwtService.generateToken(claimMap,user);
        var refreshToken = jwtService.generateRefreshToken(claimMap,user);
        revokeAllUserTokens(user);
        saveToken(user, jwtToken);
        return  new AuthResponse(jwtToken,refreshToken,new UserResponse(user));

    }

    private void revokeAllUserTokens(User user) {
        tokenRepository.revokeValidUserTokens(user.getId());
    }

    public AuthResponse refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String username;
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        refreshToken = authHeader.substring(7);
        username = jwtService.extractUsername(refreshToken);

        if(username!=null) {
            var userDetails = this.userRepository.getByUsername(username);

            if(userDetails == null)
                throw new EntityNotFoundException("User does not exist!");

            var role = userDetails.getRole();
            Map<String,Object> claimMap= new HashMap<>();
            claimMap.put("Role",role.getName());

            if(jwtService.isTokenValid(refreshToken,userDetails)) {
                var accessToken = jwtService.generateToken(claimMap,userDetails);
                revokeAllUserTokens(userDetails);
                saveToken(userDetails, accessToken);
                var authResponse = new AuthResponse(accessToken,refreshToken,new UserResponse(userDetails));
                return authResponse;
            }
        }

        return null;

    }

    public ValidationResponse validateToken(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String token;
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        token = authHeader.substring(7);

        var response = new ValidationResponse();
        response.setUsername(jwtService.extractUsername(token));
        response.setRole(jwtService.extractRole(token));

        return  response;

    }

    public AuthResponse updateUser(UserUpdateRequest userUpdateRequest) {

        var user = userRepository.getById(userUpdateRequest.getId());

        if(user == null)
            throw new EntityNotFoundException("User does not exist!");


        if(userUpdateRequest.getPassword()!=null && !userUpdateRequest.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(userUpdateRequest.getPassword()));
        }


        user.setFirstName(userUpdateRequest.getFirstname());
        user.setLastName(userUpdateRequest.getLastname());
        user.setEmail(userUpdateRequest.getEmail());
        user.setUsername(userUpdateRequest.getUsername());
        user.setPhoneNumber(userUpdateRequest.getPhoneNumber());

        Address address = null;

        if(userUpdateRequest.getAddress() != null && userUpdateRequest.getMapCoordinates() != null){
            address = new Address();
            address.setStreet(userUpdateRequest.getAddress());
            address.setMunicipality("Sarajevo");
            address.setMapCoordinates(userUpdateRequest.getMapCoordinates());
        }

        user.setAddress(address);
        userRepository.updateUser(user,address);
        Map<String,Object> claimMap= new HashMap<>();
        claimMap.put("Role",user.getRole().getName());

        var jwtToken = jwtService.generateToken(claimMap,user);
        var refreshToken = jwtService.generateRefreshToken(claimMap, user);
        revokeAllUserTokens(user);
        saveToken(user, jwtToken);
        return  new AuthResponse(jwtToken,refreshToken,new UserResponse(user));

    }


    /*
    public String getTokenFromUUID(String UUID) {
        return tokenRepository.findAll().stream()
                .filter(x -> !x.isExpired() && !x.isRevoked() && x.getUser().getUuid().equals(UUID))
                .findFirst().orElseThrow().getToken();
    }
    */
}
