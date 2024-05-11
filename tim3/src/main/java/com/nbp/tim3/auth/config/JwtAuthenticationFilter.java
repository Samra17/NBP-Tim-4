package com.nbp.tim3.auth.config;

import com.nbp.tim3.auth.service.JwtService;
import com.nbp.tim3.model.Token;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.nbp.tim3.repository.TokenRepository;

import java.io.IOException;
import java.util.Enumeration;

import static java.util.Collections.enumeration;
import static java.util.Collections.singleton;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private UserDetailsService userDetailsService;

    @Autowired
    private TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        String username = null;
        String usernameHeader = "";
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request,response);
            return;
        }


        jwtToken = authHeader.substring(7);
        try {
            username = jwtService.extractUsername(jwtToken);
            usernameHeader = username;
        } catch (ExpiredJwtException e) {
            logger.error(e.getMessage());
        }

        if(username!=null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            Token token = tokenRepository.findByToken(jwtToken);
            boolean isTokenValid = token != null && !token.isExpired() && !token.isRevoked();

            if (jwtService.isTokenValid(jwtToken, userDetails) && isTokenValid) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                        null,
                        userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);

            }

        }

        String finalUsernameHeader = usernameHeader;
        HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper((HttpServletRequest) request) {
            @Override
            public Enumeration<String> getHeaders(String name) {
                if ("username".equals(name))
                    return enumeration(singleton(finalUsernameHeader));
                return super.getHeaders(name);
            }
        };


        filterChain.doFilter(wrapper, response);



    }

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }
}