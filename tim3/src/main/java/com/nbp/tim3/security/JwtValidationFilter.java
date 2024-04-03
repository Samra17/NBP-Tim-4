package com.nbp.tim3.security;

import com.nbp.tim3.auth.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Component
public class JwtValidationFilter extends OncePerRequestFilter {
    @Autowired
    JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if(authHeader==null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        /*
        String username=httpServletRequest.getHeader("username");
        String role=httpServletRequest.getHeader("role");

         */

        String username = jwtService.extractUsername(authHeader.substring(7));
        String role = jwtService.extractRole(authHeader.substring(7));


        Set<SimpleGrantedAuthority> simpleGrantedAuthorities = new HashSet<>();
        simpleGrantedAuthorities.add(new SimpleGrantedAuthority("ROLE_"+role));

        Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, simpleGrantedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(httpServletRequest, httpServletResponse);

    }
}